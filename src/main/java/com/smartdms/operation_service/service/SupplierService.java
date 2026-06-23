package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.supplier.SupplierRequest;
import com.smartdms.operation_service.entity.Supplier;

import java.util.List;

public interface SupplierService {

    List<Supplier> getAll();

    Supplier getById(Long id);

    Supplier create(SupplierRequest supplier);

    Supplier update(Long id, SupplierRequest  supplier);

    void delete(Long id);
}
