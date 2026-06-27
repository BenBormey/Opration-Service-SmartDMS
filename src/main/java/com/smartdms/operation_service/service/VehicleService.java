package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.Vehicle.VehicleRequest;
import com.smartdms.operation_service.dto.Vehicle.VehicleResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse create(VehicleRequest request);

    List<VehicleResponse> getAll();

    VehicleResponse getById(Long id);

    VehicleResponse update(Long id, VehicleRequest request);

    void delete(Long id);
}