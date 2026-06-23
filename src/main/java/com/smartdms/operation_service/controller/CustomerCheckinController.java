package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.CustomerCheckin.CustomerCheckinRequest;
import com.smartdms.operation_service.dto.CustomerCheckin.CustomerCheckinResponse;
import com.smartdms.operation_service.service.CustomerCheckinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-checkins")
public class CustomerCheckinController {

    private final CustomerCheckinService service;

    public CustomerCheckinController(CustomerCheckinService service) {
        this.service = service;
    }

    // GET all  ->  GET /api/customer-checkins
    @GetMapping
    public ResponseEntity<List<CustomerCheckinResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET one  ->  GET /api/customer-checkins/1
    @GetMapping("/{id}")
    public ResponseEntity<CustomerCheckinResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // CREATE   ->  POST /api/customer-checkins
    @PostMapping
    public ResponseEntity<CustomerCheckinResponse> create(
            @RequestBody CustomerCheckinRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)          // 201
                .body(service.create(request));
    }

    // UPDATE   ->  PUT /api/customer-checkins/1
    @PutMapping("/{id}")
    public ResponseEntity<CustomerCheckinResponse> update(
            @PathVariable Long id,
            @RequestBody CustomerCheckinRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // DELETE   ->  DELETE /api/customer-checkins/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();   // 204
    }
}