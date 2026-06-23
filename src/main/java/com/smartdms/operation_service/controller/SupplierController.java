package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.supplier.SupplierRequest;
import com.smartdms.operation_service.entity.Supplier;
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
    public List<Supplier> getAll() {
        return supplierService.getAll();
    }

    @GetMapping("/{id}")
    public Supplier getById(@PathVariable Long id) {
        return supplierService.getById(id);
    }

    @PostMapping
    public Supplier create(@RequestBody SupplierRequest supplier) {
        return supplierService.create(supplier);
    }

    @PutMapping("/{id}")
    public Supplier update(
            @PathVariable Long id,
            @RequestBody SupplierRequest supplier) {

        return supplierService.update(id, supplier);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        supplierService.delete(id);
    }
}