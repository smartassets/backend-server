package org.korlenko.kafka.connector.mqtt.processor.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.korlenko.kafka.connector.mqtt.helper.DatabaseStatementExecutor;
import org.korlenko.kafka.connector.mqtt.helper.MachineOperationListResultSetProcessor;
import org.korlenko.kafka.connector.mqtt.model.SimpleMachineOperation;
import org.korlenko.kafka.connector.mqtt.processor.DatabaseMqttProcessor;
import org.korlenko.kafka.connector.mqtt.processor.MqttProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RfidUserDataMqttJsonProcessor implements DatabaseMqttProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RfidUserDataMqttJsonProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MqttMessage message;
    private String topic;
    private DatabaseStatementExecutor databaseExecutor;

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
            String modified = Date.from(Instant.now())
                                  .toString();
            struct.put("modified", modified);

            handleOperations(fromJson, modified);

            customSchema.fields()
                        .stream()
                        .forEach(field -> System.out.println("Field: " + field.name() + " schema: " + field.schema()));
            return new SourceRecord(null, null, kafkaTopic, null, null, null, customSchema, struct);
        } catch (IOException e) {
            LOGGER.error("Fail to map a message, error : {}", e);
            throw new RuntimeException(e);
        }
    }

    private void handleOperations(Map<String, Object> fromJson, String modified) {
        List<SimpleMachineOperation> operations = databaseExecutor.execute("Select * from operations where device_id = "
            + fromJson.get("device_id") + " and user_id = " + fromJson.get("user_id") + "", new MachineOperationListResultSetProcessor());
        LOGGER.info("No operations - creating one");
        if (operations.isEmpty()) {
            createOperation(fromJson, modified);
            return;
        }

        
        SimpleMachineOperation lastOperation = findLastRunningOperation(operations);
        if (lastOperation == null) {
            LOGGER.info("No last operation found - creating");
            createOperation(fromJson, modified);
            return;
        }
        LOGGER.info("Found operation with id " + lastOperation.getId() + " updating its updated time");
        lastOperation.setUpdatedAt(new Date(Instant.now().toEpochMilli()));
        updateOperation(lastOperation);
    }

    private void createOperation(Map<String, Object> fromJson, String modified) {
        databaseExecutor.execute("Insert into operations(device_id, user_id, created_at) VALUES(" + fromJson.get("device_id") + ", "
            + fromJson.get("user_id") + ", " + modified + "", null);
    }

    private SimpleMachineOperation findLastRunningOperation(List<SimpleMachineOperation> operations) {
        return operations.stream()
                         .filter(op -> op.getCreatedAt() != null && op.getUpdatedAt() == null)
                         .findFirst()
                         .orElse(null);
    }

    private void updateOperation(SimpleMachineOperation operation) {
        databaseExecutor.execute("UPDATE operations SET updated_at= " + operation.getUpdatedAt() + " where id = " + operation.getId() + "",
                                 null);
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

    @Override
    public void setDatabaseConnectionDetails(String connectionUrl, String connectionUsername, String connectionPassword) {
        this.databaseExecutor = new DatabaseStatementExecutor(connectionUrl, connectionUsername, connectionPassword);
    }

}
