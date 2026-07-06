package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.supplier.SupplierRequest;
import com.smartdms.operation_service.entity.Supplier;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.SupplierRepository;
import com.smartdms.operation_service.service.SupplierService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repository;

    public SupplierServiceImpl(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Supplier> getAll() {

        List<Supplier> suppliers = repository.findByIsDeletedFalse();

        if (suppliers.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found");
        }

        return suppliers;
    }

    @Override
    public Supplier getById(Long id) {

        return repository.findById(id)
                .filter(supplier -> !Boolean.TRUE.equals(supplier.getIsDeleted()))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with Id : " + id));
    }

    @Override
    public Supplier create(SupplierRequest request) {

        if (repository.existsBySupplierCode(request.getSupplierCode())) {
            throw new ResourceAlreadyExistsException(
                    "Supplier code already exists: " + request.getSupplierCode());
        }

        Supplier supplier = Supplier.builder()
                .supplierCode(request.getSupplierCode())
                .supplierName(request.getSupplierName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .isActive(request.getIsActive())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return repository.save(supplier);
    }

    @Override
    public Supplier update(Long id, SupplierRequest request) {

        Supplier existingSupplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with Id : " + id));

        if (!existingSupplier.getSupplierCode()
                .equalsIgnoreCase(request.getSupplierCode())
                && repository.existsBySupplierCode(request.getSupplierCode())) {

            throw new ResourceAlreadyExistsException(
                    "Supplier code already exists: "
                            + request.getSupplierCode());
        }

        existingSupplier.setSupplierCode(request.getSupplierCode());
        existingSupplier.setSupplierName(request.getSupplierName());
        existingSupplier.setPhone(request.getPhone());
        existingSupplier.setAddress(request.getAddress());
        existingSupplier.setIsActive(request.getIsActive());

        return repository.save(existingSupplier);
    }

    @Override
    public void delete(Long id) {

        Supplier supplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with Id : " + id));

        supplier.setIsDeleted(true);

        repository.save(supplier);
    }
}