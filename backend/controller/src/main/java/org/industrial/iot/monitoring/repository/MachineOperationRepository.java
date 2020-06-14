package org.industrial.iot.monitoring.repository;

import org.industrial.iot.monitoring.model.MachineOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineOperationRepository extends JpaRepository<MachineOperation, Long> {

}
