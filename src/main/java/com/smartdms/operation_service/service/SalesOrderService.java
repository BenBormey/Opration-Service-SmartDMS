package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.salesorder.SalesOrderRequest;
import com.smartdms.operation_service.dto.salesorder.SalesOrderResponse;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponse create(SalesOrderRequest request);

    SalesOrderResponse getById(Long id);

    List<SalesOrderResponse> getAll();

    SalesOrderResponse update(Long id, SalesOrderRequest request);

    void delete(Long id);
}