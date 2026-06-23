package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.stock.WarehouseStockRequest;
import com.smartdms.operation_service.dto.stock.WarehouseStockResponse;
import java.util.List;

public interface WarehouseStockService {
    List<WarehouseStockResponse> getAll();
    WarehouseStockResponse getById(Long id);
    WarehouseStockResponse create(WarehouseStockRequest request);
    WarehouseStockResponse update(Long id, WarehouseStockRequest request);
    void delete(Long id);
}