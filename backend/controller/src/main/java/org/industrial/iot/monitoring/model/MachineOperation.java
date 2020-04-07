package org.industrial.iot.monitoring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "machine_operations")
public class MachineOperation extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    private Employee employee;
    private Machine machine;
    private String inputMaterialType;
    private long totalInputMaterialQuantity;
    private long sucessRateMaterial;
    private long defectRateMaterial;

    private long totalWorkingHours;

    public MachineOperation() {
    }

    public MachineOperation(Employee employee, Machine machine, String inputMaterialType, long totalInputMaterialQuantity,
                            long sucessRateMaterial, long defectRateMaterial, long totalWorkingHours) {
        this.employee = employee;
        this.machine = machine;
        this.inputMaterialType = inputMaterialType;
        this.totalInputMaterialQuantity = totalInputMaterialQuantity;
        this.sucessRateMaterial = sucessRateMaterial;
        this.defectRateMaterial = defectRateMaterial;
        this.totalWorkingHours = totalWorkingHours;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getInputMaterialType() {
        return inputMaterialType;
    }

    public void setInputMaterialType(String inputMaterialType) {
        this.inputMaterialType = inputMaterialType;
    }

    public long getTotalInputMaterialQuantity() {
        return totalInputMaterialQuantity;
    }

    public void setTotalInputMaterialQuantity(long totalInputMaterialQuantity) {
        this.totalInputMaterialQuantity = totalInputMaterialQuantity;
    }

    public long getSucessRateMaterial() {
        return sucessRateMaterial;
    }

    public void setSucessRateMaterial(long sucessRateMaterial) {
        this.sucessRateMaterial = sucessRateMaterial;
    }

    public long getDefectRateMaterial() {
        return defectRateMaterial;
    }

    public void setDefectRateMaterial(long defectRateMaterial) {
        this.defectRateMaterial = defectRateMaterial;
    }

    public long getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(long totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }

}
