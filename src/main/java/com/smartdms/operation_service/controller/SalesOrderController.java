package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.salesorder.SalesOrderRequest;
import com.smartdms.operation_service.dto.salesorder.SalesOrderResponse;
import com.smartdms.operation_service.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrderResponse> create(@RequestBody SalesOrderRequest request) {
        SalesOrderResponse response = salesOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salesOrderService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderResponse>> getAll() {
        return ResponseEntity.ok(salesOrderService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderResponse> update(@PathVariable Long id,
                                                     @RequestBody SalesOrderRequest request) {
        return ResponseEntity.ok(salesOrderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salesOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}