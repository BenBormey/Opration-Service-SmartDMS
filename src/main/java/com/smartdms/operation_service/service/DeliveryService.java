package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.delivery.DeliveryRequest;
import com.smartdms.operation_service.dto.delivery.DeliveryResponse;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryService {

    DeliveryResponse create(DeliveryRequest request);

    DeliveryResponse update(Long id, DeliveryRequest request);

    DeliveryResponse getById(Long id);

    List<DeliveryResponse> getByDriverAndDate(Long driverId, LocalDate date);

    DeliveryResponse updateStatus(Long id, String status);

    void delete(Long id);
}