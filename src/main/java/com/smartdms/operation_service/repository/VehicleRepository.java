package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleNo(String vehicleNo);

    boolean existsByVehicleNo(String vehicleNo);
}