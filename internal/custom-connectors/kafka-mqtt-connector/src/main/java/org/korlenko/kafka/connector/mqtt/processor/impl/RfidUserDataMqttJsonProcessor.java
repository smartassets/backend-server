package org.korlenko.kafka.connector.mqtt.processor.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.korlenko.kafka.connector.mqtt.processor.MqttProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RfidUserDataMqttJsonProcessor implements MqttProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RfidUserDataMqttJsonProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MqttMessage message;
    private String topic;

    @Override
    public MqttProcessor process(String topic, MqttMessage message) {
        LOGGER.info("Processing message for topic \"{}\": {}", topic, message);
        this.topic = topic;
        this.message = message;
        return this;
    }

    @Override
    public SourceRecord getRecords(String kafkaTopic) {
        try {
            Schema customSchema = buildSchema();
            Map<String, Object> fromJson = convertMessageFromJson();
            Struct struct = new Struct(customSchema);
            struct.put("device_id", fromJson.get("device_id"));
            struct.put("user_id", fromJson.get("user_id"));
            struct.put("modified", Date.from(Instant.now())
                                       .toString());
            // ops = Select * from operations where device_id = fromJson.get("device_id") and user_id = fromJson.get("user_id")
            // if ops.isEmpty -> create new operation
            // lastRunninOperationForThisUser = find operation which has startTime != null and endTime = null(which is still running).
            // if lastRunninOperationForThisUser == null -> create new operation
            // if lastRunninOperationForThisUser.getEndTime() == null -> complete this operation
            // else create new operation ??? would this work ???
            // if there is already record from this device in the DB -> this is the 2nd time entering here for this device -> set end time for operation
            customSchema.fields()
                        .stream()
                        .forEach(field -> System.out.println("Field: " + field.name() + " schema: " + field.schema()));
            return new SourceRecord(null, null, kafkaTopic, null, null, null, customSchema, struct);
        } catch (IOException e) {
            LOGGER.error("Fail to map a message, error : {}", e);
            throw new RuntimeException(e);
        }
    }

    private Schema buildSchema() {
        return SchemaBuilder.struct()
                            .field("device_id", Schema.STRING_SCHEMA)
                            .field("user_id", Schema.STRING_SCHEMA)
                            .field("modified", Schema.STRING_SCHEMA)
                            .build();
    }

    private Map<String, Object> convertMessageFromJson() throws IOException, JsonParseException, JsonMappingException {
        return objectMapper.readValue(message.getPayload(), new TypeReference<Map<String, Object>>() {
        });
    }

    @Override
    public String getTopic() {
        return topic;
    }

}
