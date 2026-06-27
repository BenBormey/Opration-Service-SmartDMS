package com.smartdms.operation_service.dto.DriverAssignment;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverAssignmentResponse {

    private Long id;

    private Long driverId;
    private String driverName;

    private Long routePlanId;

    private String vehicleNo;
    private LocalDate assignedDate;
    private String status;
}