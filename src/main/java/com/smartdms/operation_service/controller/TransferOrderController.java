package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.entity.TransferOrder;
import com.smartdms.operation_service.service.ITransferOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transfer-orders")
@RequiredArgsConstructor
public class TransferOrderController {

    private final ITransferOrderService transferOrderService;

    // Create Transfer Order (REQUEST)
    @PostMapping
    public ResponseEntity<TransferOrder> create(@RequestBody TransferOrder transferOrder) {
        TransferOrder saved = transferOrderService.save(transferOrder);
        // 201 Created with a Location header is more correct than 200 for a create
        return ResponseEntity
                .created(URI.create("/api/transfer-orders/" + saved.getId()))
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<TransferOrder>> getAll() {
        return ResponseEntity.ok(transferOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferOrder> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.findById(id));
    }

    // Update — uses the dedicated update method, not save
    @PutMapping("/{id}")
    public ResponseEntity<TransferOrder> update(
            @PathVariable Long id,
            @RequestBody TransferOrder transferOrder) {
        return ResponseEntity.ok(transferOrderService.update(id, transferOrder));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<TransferOrder> approve(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.approve(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<TransferOrder> ship(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.ship(id));
    }

    @PutMapping("/{id}/receive")
    public ResponseEntity<TransferOrder> receive(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.receive(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TransferOrder> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.cancel(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transferOrderService.delete(id);
        return ResponseEntity.noContent().build();   // 204, standard for delete
    }
}