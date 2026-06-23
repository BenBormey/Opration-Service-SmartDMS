package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.stock.WarehouseStockRequest;
import com.smartdms.operation_service.dto.stock.WarehouseStockResponse;
import com.smartdms.operation_service.service.WarehouseStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse-stocks")
public class WarehouseStockController {

    private final WarehouseStockService service;

    public WarehouseStockController(WarehouseStockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseStockResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseStockResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<WarehouseStockResponse> create(@RequestBody WarehouseStockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseStockResponse> update(@PathVariable Long id,
                                                         @RequestBody WarehouseStockRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}