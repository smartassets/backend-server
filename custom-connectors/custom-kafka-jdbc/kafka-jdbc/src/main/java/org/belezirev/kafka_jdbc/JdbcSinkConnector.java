package org.belezirev.kafka_jdbc;

import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcSinkConnector extends SinkConnector {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSinkConnector.class);

    public String version() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public void start(Map<String, String> props) {
    }

    @Override
    public Class<? extends Task> taskClass() {
        return null;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        return null;
    }

    @Override
    public void stop() {
    }

    @Override
    public ConfigDef config() {
        return null;
    }

}
