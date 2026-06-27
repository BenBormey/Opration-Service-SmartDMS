package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Vehicle.VehicleRequest;
import com.smartdms.operation_service.dto.Vehicle.VehicleResponse;
import com.smartdms.operation_service.entity.Vehicle;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.VehicleRepository;
import com.smartdms.operation_service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponse create(VehicleRequest request) {

        if (vehicleRepository.existsByVehicleNo(request.getVehicleNo())) {
            throw new ResourceAlreadyExistsException(
                    "Vehicle already exists with number: " + request.getVehicleNo());
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNo(request.getVehicleNo());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setModel(request.getModel());
        vehicle.setStatus(request.getStatus());
        vehicle.setCreatedAt(LocalDateTime.now());

        vehicle = vehicleRepository.save(vehicle);

        return mapToResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> getAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleResponse getById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + id));

        return mapToResponse(vehicle);
    }

    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicle.setVehicleNo(request.getVehicleNo());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setModel(request.getModel());
        vehicle.setStatus(request.getStatus());

        vehicle = vehicleRepository.save(vehicle);

        return mapToResponse(vehicle);
    }

    @Override
    public void delete(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicleRepository.delete(vehicle);
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {

        VehicleResponse response = new VehicleResponse();

        response.setId(vehicle.getId());
        response.setVehicleNo(vehicle.getVehicleNo());
        response.setVehicleType(vehicle.getVehicleType());
        response.setModel(vehicle.getModel());
        response.setStatus(vehicle.getStatus());
        response.setCreatedAt(vehicle.getCreatedAt());

        return response;
    }
}