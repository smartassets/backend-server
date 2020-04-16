package org.korlenko.kafka.connector.mqtt.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseStatementExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseStatementExecutor.class);

    private String connectionUrl;
    private String connectionUsername;
    private String connectionPassword;

    public DatabaseStatementExecutor(String connectionUrl, String connectionUsername, String connectionPassword) {
        this.connectionUrl = connectionUrl;
        this.connectionUsername = connectionUsername;
        this.connectionPassword = connectionPassword;
    }

    public <T> T execute(String sql, ResultSetProcessor<T> processor) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            T result = processor.process(resultSet);
            resultSet.close();
            return result;
        } catch (SQLException e) {
            LOGGER.error("Error executing database statement", e);
        } finally {
            closeQuietly(statement);
            closeQuietly(connection);
        }
        
        return null;
    }

    private void closeQuietly(AutoCloseable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (Exception e) {
            }
        }
    }

}
