package com.smartdms.operation_service.dto.driverassignment;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverAssignmentRequest {

    private Long driverId;

    private Long routePlanId;

    private String vehicleNo;

    private LocalDate assignedDate;

    private String status;
}