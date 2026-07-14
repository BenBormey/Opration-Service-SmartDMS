package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.supplier.SupplierRequest;
import com.smartdms.operation_service.dto.supplier.SupplierResponse;
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
    public List<SupplierResponse> getAll() {

        List<Supplier> suppliers = repository.findByIsDeletedFalse();

        if (suppliers.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found");
        }

        return suppliers.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SupplierResponse getById(Long id) {

        Supplier supplier = repository.findById(id)
                .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with Id : " + id));

        return toResponse(supplier);
    }

    @Override
    public SupplierResponse create(SupplierRequest request) {

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

        return toResponse(repository.save(supplier));
    }

    @Override
    public SupplierResponse update(Long id, SupplierRequest request) {

        Supplier existingSupplier = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier not found with Id : " + id));

        if (!existingSupplier.getSupplierCode().equalsIgnoreCase(request.getSupplierCode())
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

        return toResponse(repository.save(existingSupplier));
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

    private SupplierResponse toResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .supplierCode(supplier.getSupplierCode())
                .supplierName(supplier.getSupplierName())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .isActive(supplier.getIsActive())
                .build();
    }
}
