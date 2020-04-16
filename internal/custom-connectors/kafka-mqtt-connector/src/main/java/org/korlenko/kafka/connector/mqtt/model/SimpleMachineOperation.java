package org.korlenko.kafka.connector.mqtt.model;

import java.util.Date;

public class SimpleMachineOperation {

    private long id;
    private String employeeId;
    private String deviceId;
    private Date createdAt;
    private Date updatedAt;

    public SimpleMachineOperation(long id, String employeeId, String deviceId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.deviceId = deviceId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    

}
