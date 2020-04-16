package org.korlenko.kafka.connector.mqtt.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.korlenko.kafka.connector.mqtt.model.SimpleMachineOperation;

public class MachineOperationListResultSetProcessor implements ResultSetProcessor<List<SimpleMachineOperation>> {

    @Override
    public List<SimpleMachineOperation> process(ResultSet resultSet) throws SQLException {
        List<SimpleMachineOperation> operations = new ArrayList<>();
        while (resultSet.next()) {
            long operationId = resultSet.getLong("id");
            String employeeId = resultSet.getString("employee_id");
            String machineNumber = resultSet.getString("machineNumber");
            Timestamp createdAt = resultSet.getTimestamp("created_at");
            Timestamp updatedAt = resultSet.getTimestamp("updated_at");
            operations.add(new SimpleMachineOperation(operationId,
                                                      employeeId,
                                                      machineNumber,
                                                      new Date(createdAt.getTime()),
                                                      new Date(updatedAt.getTime())));
        }
        return operations;
    }

}
