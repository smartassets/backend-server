package org.industrial.iot.monitoring.repository;

import org.industrial.iot.monitoring.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, String>{

}
