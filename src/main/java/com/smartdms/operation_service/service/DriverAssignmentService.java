package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.driverassignment.DriverAssignmentRequest;
import com.smartdms.operation_service.dto.driverassignment.DriverAssignmentResponse;

import java.util.List;

public interface DriverAssignmentService {

    DriverAssignmentResponse create(DriverAssignmentRequest request);

    DriverAssignmentResponse update(Long id, DriverAssignmentRequest request);

    DriverAssignmentResponse getById(Long id);

    List<DriverAssignmentResponse> getAll();

    List<DriverAssignmentResponse> getByDriverId(Long driverId);

    void delete(Long id);
}