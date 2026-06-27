package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.DriverAssignment.DriverAssignmentRequest;
import com.smartdms.operation_service.dto.DriverAssignment.DriverAssignmentResponse;

import java.util.List;

public interface DriverAssignmentService {

    DriverAssignmentResponse create(DriverAssignmentRequest request);

    DriverAssignmentResponse update(Long id, DriverAssignmentRequest request);

    DriverAssignmentResponse getById(Long id);

    List<DriverAssignmentResponse> getAll();

    List<DriverAssignmentResponse> getByDriverId(Long driverId);

    void delete(Long id);
}