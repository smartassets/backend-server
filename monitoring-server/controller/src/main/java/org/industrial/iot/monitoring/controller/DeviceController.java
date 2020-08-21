package org.industrial.iot.monitoring.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.industrial.iot.monitoring.model.Device;
import org.industrial.iot.monitoring.model.Device.DeviceStatus;
import org.industrial.iot.monitoring.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping("/api/devices")
    public Device createDevice(@Valid @RequestBody Device device) {
        return deviceRepository.save(device);
    }

    @GetMapping("/api/devices/{deviceGuid}")
    public ResponseEntity<?> getDeviceByGuid(@PathVariable String deviceGuid) {
        Optional<Device> device = findDevice(deviceGuid);
        if (device.isPresent()) {
            determineDeviceStatus(device.get());
            return ResponseEntity.ok(device.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(String.format("Device with guid \"$s\" not found", deviceGuid));
    }

    @PutMapping("/api/devices/{deviceGuid}")
    public ResponseEntity<?> updateDevice(@PathVariable String deviceGuid, @Valid @RequestBody Device device) {
        Optional<Device> foundDevice = findDevice(deviceGuid);
        if (foundDevice.isPresent()) {
            return ResponseEntity.ok(updateDevice(foundDevice.get(), device));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(String.format("Device with guid \"$s\" not found", deviceGuid));
    }

    @DeleteMapping("/api/devices/{deviceGuid}")
    public ResponseEntity<?> deleteDeive(@PathVariable String deviceGuid) {
        Optional<Device> device = findDevice(deviceGuid);
        if (device.isPresent()) {
            deviceRepository.delete(device.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                 .build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(String.format("Device with guid \"$s\" not found", deviceGuid));
    }

    @GetMapping("/api/devices")
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    private Optional<Device> findDevice(String deviceGuid) {
        return deviceRepository.findById(deviceGuid);
    }

    private void determineDeviceStatus(Device device) {
        long lastUpdatedDeviceTime = System.currentTimeMillis() - device.getUpdatedAt()
                                                                        .getTime();
        if (lastUpdatedDeviceTime >= 10 * 1000) {
            device.setStatus(DeviceStatus.STOPPED);
        }

    }

    private Device updateDevice(Device foundDevice, Device device) {
        foundDevice.setDescription(device.getDescription() == null ? foundDevice.getDescription() : device.getDescription());
        foundDevice.setStatus(device.getStatus());
        foundDevice.setUpdatedAt(new Date(System.currentTimeMillis()));
        return deviceRepository.save(foundDevice);
    }
}
