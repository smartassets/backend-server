package org.korlenko.kafka.connector.mqtt.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetProcessor<T> {

    T process(ResultSet resultSet) throws SQLException;
    
}
