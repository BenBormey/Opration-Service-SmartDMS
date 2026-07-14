package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.driverassignment.DriverAssignmentRequest;
import com.smartdms.operation_service.dto.driverassignment.DriverAssignmentResponse;
import com.smartdms.operation_service.entity.DriverAssignment;
import com.smartdms.operation_service.entity.RoutePlan;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.DriverAssignmentRepository;
import com.smartdms.operation_service.repository.RoutePlanRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.DriverAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverAssignmentServiceImpl implements DriverAssignmentService {

    private final DriverAssignmentRepository driverAssignmentRepository;
    private final UserRepository userRepository;
    private final RoutePlanRepository routePlanRepository;

    @Override
    public DriverAssignmentResponse create(DriverAssignmentRequest request) {

        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver not found"));

        RoutePlan routePlan = routePlanRepository.findById(request.getRoutePlanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Route Plan not found"));

        DriverAssignment assignment = new DriverAssignment();
        assignment.setDriverId(driver.getId());
        assignment.setRoutePlanId(routePlan.getId());
        assignment.setVehicleNo(request.getVehicleNo());
        assignment.setAssignedDate(request.getAssignedDate());
        assignment.setStatus(request.getStatus());

        assignment = driverAssignmentRepository.save(assignment);

        return mapToResponse(assignment);
    }

    @Override
    public DriverAssignmentResponse update(Long id, DriverAssignmentRequest request) {

        DriverAssignment assignment = driverAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver Assignment not found"));

        assignment.setDriverId(request.getDriverId());
        assignment.setRoutePlanId(request.getRoutePlanId());
        assignment.setVehicleNo(request.getVehicleNo());
        assignment.setAssignedDate(request.getAssignedDate());
        assignment.setStatus(request.getStatus());

        assignment = driverAssignmentRepository.save(assignment);

        return mapToResponse(assignment);
    }

    @Override
    public DriverAssignmentResponse getById(Long id) {

        DriverAssignment assignment = driverAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver Assignment not found"));

        return mapToResponse(assignment);
    }

    @Override
    public List<DriverAssignmentResponse> getAll() {

        return driverAssignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverAssignmentResponse> getByDriverId(Long driverId) {

        return driverAssignmentRepository.findByDriverId(driverId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        DriverAssignment assignment = driverAssignmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver Assignment not found"));

        driverAssignmentRepository.delete(assignment);
    }

    private DriverAssignmentResponse mapToResponse(DriverAssignment assignment) {

        DriverAssignmentResponse response = new DriverAssignmentResponse();

        response.setId(assignment.getId());
        response.setDriverId(assignment.getDriverId());
        response.setRoutePlanId(assignment.getRoutePlanId());
        response.setVehicleNo(assignment.getVehicleNo());
        response.setAssignedDate(assignment.getAssignedDate());
        response.setStatus(assignment.getStatus());

        userRepository.findById(assignment.getDriverId())
                .ifPresent(driver ->
                        response.setDriverName(driver.getFullName()));

        return response;
    }
}