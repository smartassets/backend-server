package org.industrial.iot.monitoring.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.industrial.iot.monitoring.model.Machine;
import org.industrial.iot.monitoring.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachineController {
    @Autowired
    private MachineRepository machineRepository;

    @PostMapping("/api/machines")
    public Machine addMachine(@Valid @RequestBody Machine machine) {
        return machineRepository.save(machine);
    }

    @GetMapping("/api/machines")
    public List<Machine> getMachines() {
        return machineRepository.findAll();
    }

    @GetMapping("/api/machines/{id}")
    public Machine getMachineById(@PathVariable String id) {
        Optional<Machine> machineOptional = machineRepository.findById(id);
        if (machineOptional.isPresent()) {
            return machineOptional.get();
        }
        
        return null;
    }
}
