package org.industrial.iot.monitoring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "devices")
public class Device extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String deviceGuid;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status;

    public Device() {
    }

    public Device(String deviceGuid, String description, DeviceStatus status) {
        this.deviceGuid = deviceGuid;
        this.description = description;
        this.status = status;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public String getDescription() {
        return description;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setDeviceGuid(String deviceGuid) {
        this.deviceGuid = deviceGuid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public enum DeviceStatus {
        RUNNING, STOPPED, UPDATING
    }
}
