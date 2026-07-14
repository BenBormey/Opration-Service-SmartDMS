package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.supplier.SupplierRequest;
import com.smartdms.operation_service.dto.supplier.SupplierResponse;
import com.smartdms.operation_service.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public List<SupplierResponse> getAll() {
        return supplierService.getAll();
    }

    @GetMapping("/{id}")
    public SupplierResponse getById(@PathVariable Long id) {
        return supplierService.getById(id);
    }

    @PostMapping
    public SupplierResponse create(@RequestBody SupplierRequest request) {
        return supplierService.create(request);
    }

    @PutMapping("/{id}")
    public SupplierResponse update(
            @PathVariable Long id,
            @RequestBody SupplierRequest request) {

        return supplierService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        supplierService.delete(id);
    }
}
