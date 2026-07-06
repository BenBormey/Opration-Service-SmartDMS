package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.Collection.CollectionRequest;
import com.smartdms.operation_service.dto.Collection.CollectionResponse;
import com.smartdms.operation_service.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<CollectionResponse> create(@RequestBody CollectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(collectionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CollectionResponse>> getAll() {
        return ResponseEntity.ok(collectionService.getAll());
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<CollectionResponse>> getByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(collectionService.getByInvoiceId(invoiceId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        collectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}