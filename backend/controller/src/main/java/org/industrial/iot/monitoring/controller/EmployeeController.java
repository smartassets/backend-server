package org.industrial.iot.monitoring.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.industrial.iot.monitoring.model.Employee;
import org.industrial.iot.monitoring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/api/employees")
    public Employee addEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @GetMapping("/api/employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/api/employees/{id}")
    public Employee getEmployeeById(@PathVariable long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        
        return null;
    }

}
