package org.industrial.iot.monitoring.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "machines")
public class Machine extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String machineNumber;

    private String location;

    @Enumerated(EnumType.STRING)
    private MachineType type;
    
    @OneToMany(mappedBy = "machine")
    private List<MachineOperation> machineOperations;

    public Machine() {
    }

    public Machine(String machineNumber, String location, MachineType type) {
        this.machineNumber = machineNumber;
        this.location = location;
        this.type = type;
    }

    public String getMachineNumber() {
        return machineNumber;
    }

    public String getLocation() {
        return location;
    }

    public MachineType getType() {
        return type;
    }

    private enum MachineType {
        LENSES, HIGH_PRECISION_LENCES
    }

}
