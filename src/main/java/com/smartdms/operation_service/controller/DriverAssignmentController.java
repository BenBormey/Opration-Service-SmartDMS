package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.driverassignment.DriverAssignmentRequest;
import com.smartdms.operation_service.dto.driverassignment.DriverAssignmentResponse;
import com.smartdms.operation_service.service.DriverAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver-assignments")
@RequiredArgsConstructor
public class DriverAssignmentController {

    private final DriverAssignmentService driverAssignmentService;

    @PostMapping
    public ResponseEntity<DriverAssignmentResponse> create(
            @RequestBody DriverAssignmentRequest request) {

        return new ResponseEntity<>(
                driverAssignmentService.create(request),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverAssignmentResponse> update(
            @PathVariable Long id,
            @RequestBody DriverAssignmentRequest request) {

        return ResponseEntity.ok(
                driverAssignmentService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverAssignmentResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                driverAssignmentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DriverAssignmentResponse>> getAll() {

        return ResponseEntity.ok(
                driverAssignmentService.getAll());
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<DriverAssignmentResponse>> getByDriverId(
            @PathVariable Long driverId) {

        return ResponseEntity.ok(
                driverAssignmentService.getByDriverId(driverId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        driverAssignmentService.delete(id);

        return ResponseEntity.ok(
                "Driver Assignment deleted successfully");
    }
}