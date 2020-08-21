package org.industrial.iot.monitoring.repository;

import org.industrial.iot.monitoring.model.MachineOperationParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineOperationParameterRepository extends JpaRepository<MachineOperationParameter, Long> {

}
