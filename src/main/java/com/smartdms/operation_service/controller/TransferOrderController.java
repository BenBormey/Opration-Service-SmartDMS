package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.transferorder.TransferOrderRequest;
import com.smartdms.operation_service.dto.transferorder.TransferOrderResponse;
import com.smartdms.operation_service.service.TransferOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transfer-orders")
@RequiredArgsConstructor
public class TransferOrderController {

    private final TransferOrderService transferOrderService;

    @PostMapping
    public ResponseEntity<TransferOrderResponse> create(@RequestBody TransferOrderRequest request) {
        TransferOrderResponse saved = transferOrderService.create(request);
        // 201 Created with a Location header is more correct than 200 for a create
        return ResponseEntity
                .created(URI.create("/api/transfer-orders/" + saved.getId()))
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<TransferOrderResponse>> getAll() {
        return ResponseEntity.ok(transferOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferOrderResponse> update(
            @PathVariable Long id,
            @RequestBody TransferOrderRequest request) {
        return ResponseEntity.ok(transferOrderService.update(id, request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<TransferOrderResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.approve(id));
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<TransferOrderResponse> ship(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.ship(id));
    }

    @PutMapping("/{id}/receive")
    public ResponseEntity<TransferOrderResponse> receive(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.receive(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TransferOrderResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(transferOrderService.cancel(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transferOrderService.delete(id);
        return ResponseEntity.noContent().build();   // 204, standard for delete
    }
}
