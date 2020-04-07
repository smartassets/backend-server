package org.industrial.iot.monitoring.controller;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.industrial.iot.monitoring.model.MachineOperation;
import org.industrial.iot.monitoring.repository.MachineOperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MachineOperationController {

    public static final Logger LOGGER = LoggerFactory.getLogger(MachineOperationController.class);

    @Autowired
    private MachineOperationRepository machineOperationRepository;

    @PostMapping("/api/operations")
    public MachineOperation registerMachineOperation(@Valid @RequestBody MachineOperation device) {
        return machineOperationRepository.save(device);
    }

    @RequestMapping(path = "/api/operations", method = RequestMethod.GET)
    public List<MachineOperation> getMachineOPerations(@RequestParam(required = false, name = "searchType") String searchType,
                                                       @RequestParam(required = false, name = "criteria") String criteria,
                                                       @RequestParam(required = false, name = "statistics") String statistics) {
        LOGGER.info("Search type: " + searchType);
        LOGGER.info("Statistics: " + statistics);
        if ("employee".equals(searchType)) {
            if (statistics != null && Boolean.valueOf(statistics)) {
                return Arrays.asList(getOverallStatisticsForEmployee(criteria));
            }
            return machineOperationRepository.findAllByEmployeeId(criteria);
        }

        if ("machine".equals(searchType)) {
            return machineOperationRepository.findAllByMachineId(criteria);
        }

        if (statistics != null && Boolean.valueOf(statistics)) {
            return Arrays.asList(getOverallStatistics());
        }

        return machineOperationRepository.findAll();
    }

    @PutMapping("/api/operations/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody MachineOperation machineOperation) {
        Optional<MachineOperation> operationOptional = machineOperationRepository.findById(id);
        if (!operationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(MessageFormat.format("Operation with id {0} not found", id));
        }
        MachineOperation operation = operationOptional.get();
        updateOperation(machineOperation, operation);
        machineOperationRepository.save(operation);
        return ResponseEntity.ok(operation);
    }


    private MachineOperation getOverallStatistics() {
        List<MachineOperation> allOperations = machineOperationRepository.findAll();

        long overallSuccessRate = allOperations.stream()
                                               .mapToLong(MachineOperation::getSucessRateMaterial)
                                               .sum();
        long overallDefectRate = allOperations.stream()
                                              .mapToLong(MachineOperation::getDefectRateMaterial)
                                              .sum();
        long overallMaterial = allOperations.stream()
                                            .mapToLong(MachineOperation::getTotalInputMaterialQuantity)
                                            .sum();
        MachineOperation operation = new MachineOperation();
        operation.setSucessRateMaterial(overallSuccessRate);
        operation.setDefectRateMaterial(overallDefectRate);
        operation.setTotalInputMaterialQuantity(overallMaterial);
        return operation;
    }

    private MachineOperation getOverallStatisticsForEmployee(String criteria) {
        List<MachineOperation> allOperationsByEmployee = machineOperationRepository.findAllByEmployeeId(criteria);
        MachineOperation result = new MachineOperation();
        for (MachineOperation machineOperation : allOperationsByEmployee) {
            result.setSucessRateMaterial(result.getSucessRateMaterial() + machineOperation.getSucessRateMaterial());
            result.setDefectRateMaterial(result.getDefectRateMaterial() + machineOperation.getDefectRateMaterial());
            result.setTotalWorkingHours(result.getTotalWorkingHours() + result.getTotalWorkingHours());
        }
        return result;
    }

    private void updateOperation(MachineOperation machineOperation, MachineOperation operation) {
        updateSuccessRate(operation, machineOperation.getSucessRateMaterial());
        updateDefectRate(operation, machineOperation.getDefectRateMaterial());
    }

    private void updateSuccessRate(MachineOperation operation, long additionalSuccessRate) {
        long newSuccessRate = operation.getSucessRateMaterial() + additionalSuccessRate;
        operation.setSucessRateMaterial(newSuccessRate);
    }

    private void updateDefectRate(MachineOperation operation, long additionalDefectRate) {
        long newDefectRate = operation.getDefectRateMaterial() + additionalDefectRate;
        operation.setDefectRateMaterial(newDefectRate);
    }

}
