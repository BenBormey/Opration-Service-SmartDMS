package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.stock.StockLedgerRequest;
import com.smartdms.operation_service.dto.stock.StockLedgerResponse;
import com.smartdms.operation_service.service.StockLedgerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-ledgers")
public class StockLedgerController {

    private final StockLedgerService service;

    public StockLedgerController(StockLedgerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<StockLedgerResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockLedgerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<StockLedgerResponse> create(@RequestBody StockLedgerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockLedgerResponse> update(@PathVariable Long id,
                                                      @RequestBody StockLedgerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}