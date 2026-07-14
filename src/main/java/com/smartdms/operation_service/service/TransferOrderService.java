package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.transferorder.TransferOrderRequest;
import com.smartdms.operation_service.dto.transferorder.TransferOrderResponse;

import java.util.List;

public interface TransferOrderService {

    TransferOrderResponse create(TransferOrderRequest request);

    TransferOrderResponse update(Long id, TransferOrderRequest request);

    List<TransferOrderResponse> getAll();

    TransferOrderResponse getById(Long id);

    void delete(Long id);

    TransferOrderResponse approve(Long id);

    TransferOrderResponse ship(Long id);

    TransferOrderResponse receive(Long id);

    TransferOrderResponse cancel(Long id);
}
