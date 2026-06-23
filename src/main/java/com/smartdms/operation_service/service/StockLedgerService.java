package com.smartdms.operation_service.service;


import com.smartdms.operation_service.dto.stock.StockLedgerRequest;
import com.smartdms.operation_service.dto.stock.StockLedgerResponse;
import java.util.List;

public interface StockLedgerService {
    List<StockLedgerResponse> getAll();
    StockLedgerResponse getById(Long id);
    StockLedgerResponse create(StockLedgerRequest request);
    StockLedgerResponse update(Long id, StockLedgerRequest request);
    void delete(Long id);
}