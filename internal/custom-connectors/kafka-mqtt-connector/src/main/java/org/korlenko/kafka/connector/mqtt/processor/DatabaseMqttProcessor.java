package org.korlenko.kafka.connector.mqtt.processor;

public interface DatabaseMqttProcessor extends MqttProcessor{

    public void setDatabaseConnectionDetails(String connectionUrl, String connectionUsername, String connectionPassword);
    
}
