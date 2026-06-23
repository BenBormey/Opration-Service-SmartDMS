package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.stock.StockRequest;
import com.smartdms.operation_service.dto.stock.StockResponse;

import java.util.List;

public interface StockService {

    StockResponse create(StockRequest request);

    StockResponse getById(Long id);

    List<StockResponse> getAll();

    StockResponse update(Long id, StockRequest request);

    void delete(Long id);
}