package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.vehicle.VehicleRequest;
import com.smartdms.operation_service.dto.vehicle.VehicleResponse;
import com.smartdms.operation_service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> create(
            @RequestBody VehicleRequest request) {
        return new ResponseEntity<>(
                vehicleService.create(request),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(
            @PathVariable Long id,
            @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(
                vehicleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        vehicleService.delete(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}