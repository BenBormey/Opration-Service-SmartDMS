package com.smartdms.operation_service.dto.Driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverVehicleInfoResponse {
    private String licensePlate;   // "2AA-1234"
    private String vehicleModel;   // "Toyota Hino"
    private String routeName;      // "ROUTE-08"
    private Long vehicleId;
    private Long routePlanId;
    private Long driverAssignmentId;
}