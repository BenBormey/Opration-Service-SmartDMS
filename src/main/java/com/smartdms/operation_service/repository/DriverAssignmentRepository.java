package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.DriverAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverAssignmentRepository extends JpaRepository<DriverAssignment, Long> {

    List<DriverAssignment> findByDriverId(Long driverId);

    List<DriverAssignment> findByRoutePlanId(Long routePlanId);

    List<DriverAssignment> findByStatus(String status);

    List<DriverAssignment> findByDriverIdAndStatus(Long driverId, String status);
}