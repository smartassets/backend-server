package org.industrial.iot.monitoring.repository;

import org.industrial.iot.monitoring.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>{

}
