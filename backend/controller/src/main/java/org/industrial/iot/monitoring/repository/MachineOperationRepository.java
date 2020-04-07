package org.industrial.iot.monitoring.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.industrial.iot.monitoring.model.MachineOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineOperationRepository extends JpaRepository<MachineOperation, Long> {

    public default List<MachineOperation> findAllByEmployeeId(String employeeId) {
        List<MachineOperation> allOperations = findAll();
        return allOperations.stream()
                            .filter(op -> op.getEmployee()
                                            .getId()
                                            .equals(employeeId))
                            .collect(Collectors.toList());
    }

    public default List<MachineOperation> findAllByMachineId(String machineId) {
        List<MachineOperation> allOperations = findAll();
        return allOperations.stream()
                            .filter(op -> op.getMachine()
                                            .getMachineNumber()
                                            .equals(machineId))
                            .collect(Collectors.toList());
    }
}
