package org.industrial.iot.monitoring.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "machine_operation_parameters")
public class MachineOperationParameter extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    private long rpmUpperSpindle;
    private long rpmLowerSpindle;
    private int oscillationEdge;
    private int polishingLiquidTemperature;
    private int pressureUpperSpindle;
    private int operationDetailTimer;

    public MachineOperationParameter() {
    }

    public MachineOperationParameter(long id, List<MachineOperation> operations, long rpmUpperSpindle, long rpmLowerSpindle,
                                      int oscillationEdge, int polishingLiquidTemperature, int pressureUpperSpindle,
                                      int operationDetailTimer) {
        this.id = id;
        this.rpmUpperSpindle = rpmUpperSpindle;
        this.rpmLowerSpindle = rpmLowerSpindle;
        this.oscillationEdge = oscillationEdge;
        this.polishingLiquidTemperature = polishingLiquidTemperature;
        this.pressureUpperSpindle = pressureUpperSpindle;
        this.operationDetailTimer = operationDetailTimer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public List<MachineOperation> getOperations() {
//        return operations;
//    }
//
//    public void setOperations(List<MachineOperation> operations) {
//        this.operations = operations;
//    }

    public long getRpmUpperSpindle() {
        return rpmUpperSpindle;
    }

    public void setRpmUpperSpindle(long rpmUpperSpindle) {
        this.rpmUpperSpindle = rpmUpperSpindle;
    }

    public long getRpmLowerSpindle() {
        return rpmLowerSpindle;
    }

    public void setRpmLowerSpindle(long rpmLowerSpindle) {
        this.rpmLowerSpindle = rpmLowerSpindle;
    }

    public int getOscillationEdge() {
        return oscillationEdge;
    }

    public void setOscillationEdge(int oscillationEdge) {
        this.oscillationEdge = oscillationEdge;
    }

    public int getPolishingLiquidTemperature() {
        return polishingLiquidTemperature;
    }

    public void setPolishingLiquidTemperature(int polishingLiquidTemperature) {
        this.polishingLiquidTemperature = polishingLiquidTemperature;
    }

    public int getPressureUpperSpindle() {
        return pressureUpperSpindle;
    }

    public void setPressureUpperSpindle(int pressureUpperSpindle) {
        this.pressureUpperSpindle = pressureUpperSpindle;
    }

    public int getOperationDetailTimer() {
        return operationDetailTimer;
    }

    public void setOperationDetailTimer(int operationDetailTimer) {
        this.operationDetailTimer = operationDetailTimer;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
