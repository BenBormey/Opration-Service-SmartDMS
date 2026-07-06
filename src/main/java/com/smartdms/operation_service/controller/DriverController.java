package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.Driver.DriverStopResponse;
import com.smartdms.operation_service.dto.Driver.DriverSummaryResponse;

import com.smartdms.operation_service.dto.Driver.DriverVehicleInfoResponse;
import com.smartdms.operation_service.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;


    @GetMapping("/stops")
    public ResponseEntity<List<DriverStopResponse>> stops(
            @RequestParam Long driverId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                driverService.getStops(driverId, date != null ? date : LocalDate.now()));
    }


    @GetMapping("/summary")
    public ResponseEntity<DriverSummaryResponse> summary(
            @RequestParam Long driverId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                driverService.getSummary(driverId, date != null ? date : LocalDate.now()));
    }
    @GetMapping("/vehicle-info")
    public ResponseEntity<DriverVehicleInfoResponse> vehicleInfo(
            @RequestParam Long driverId) {
        return ResponseEntity.ok(driverService.getVehicleInfo(driverId));
    }
}