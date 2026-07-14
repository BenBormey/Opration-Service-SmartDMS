package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.supplier.SupplierRequest;
import com.smartdms.operation_service.dto.supplier.SupplierResponse;

import java.util.List;

public interface SupplierService {

    List<SupplierResponse> getAll();

    SupplierResponse getById(Long id);

    SupplierResponse create(SupplierRequest request);

    SupplierResponse update(Long id, SupplierRequest request);

    void delete(Long id);
}
