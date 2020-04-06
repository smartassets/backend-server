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

/**
 * Json processor implementation
 *
 * @author Kseniia Orlenko
 * @version 5/18/18
 */
public class EnvironmentDataMqttJsonProcessor implements MqttProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentDataMqttJsonProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MqttMessage message;
    private String topic;

    @Override
    public MqttProcessor process(String topic, MqttMessage message) {
        LOGGER.info("Processing data topic: [{}]; message [{}]", topic, message);
        this.message = message;
        this.topic = topic;
        return this;
    }

    /**
     * Transforms MQTT payload from byte[] to json string, creates the SourceRecord and returns it
     *
     * @param #kafkaTopic the kafka topic
     * @return the SourceRecord
     */
    @Override
    public SourceRecord getRecords(String kafkaTopic) {
        try {
            Schema customSchema = buildSchema();
            Map<String, Object> fromJson = convertMessageFromJson();
            Struct struct = new Struct(customSchema);
            struct.put("device_id", fromJson.get("device_id"));
            struct.put("data", fromJson.get("data"));
            struct.put("modified", Date.from(Instant.now())
                                       .toString());
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
        Schema customSchema = SchemaBuilder.struct()
                                           .field("device_id", Schema.STRING_SCHEMA)
                                           .field("data", Schema.STRING_SCHEMA)
                                           .field("modified", Schema.STRING_SCHEMA)
                                           .build();
        return customSchema;
    }

    private Map<String, Object> convertMessageFromJson() throws IOException, JsonParseException, JsonMappingException {
        Map<String, Object> fromBytes = objectMapper.readValue(message.getPayload(), new TypeReference<Map<String, Object>>() {
        });
        return fromBytes;
    }

    @Override
    public String getTopic() {
        return topic;
    }

}
