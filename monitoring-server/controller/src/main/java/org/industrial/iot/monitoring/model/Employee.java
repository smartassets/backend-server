package org.industrial.iot.monitoring.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private EmployeePosition position;
    
    @OneToMany(mappedBy="employee")
    private List<MachineOperation> employeeOperations;

    public Employee() {
    }

    public Employee(String id, String name, EmployeePosition position) {
        super();
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    private enum EmployeePosition {
        WORKER, SUPERVISER, ADMINISTRATOR
    }
}
