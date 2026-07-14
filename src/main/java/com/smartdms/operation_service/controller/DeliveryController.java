package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.delivery.DeliveryRequest;
import com.smartdms.operation_service.dto.delivery.DeliveryResponse;
import com.smartdms.operation_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(
            @RequestBody DeliveryRequest request) {

        return new ResponseEntity<>(
                deliveryService.create(request),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryResponse> update(
            @PathVariable Long id,
            @RequestBody DeliveryRequest request) {

        return ResponseEntity.ok(
                deliveryService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                deliveryService.getById(id));
    }

    // GET /api/deliveries?driverId=8&date=2026-07-01
    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getByDriverAndDate(
            @RequestParam Long driverId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(
                deliveryService.getByDriverAndDate(
                        driverId, date != null ? date : LocalDate.now()));
    }

    // PATCH /api/deliveries/{id}/status   body: { "status": "COMPLETED" }
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                deliveryService.updateStatus(id, body.get("status")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        deliveryService.delete(id);

        return ResponseEntity.ok(
                "Delivery deleted successfully");
    }
}