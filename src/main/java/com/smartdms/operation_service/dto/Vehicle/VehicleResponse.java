package com.smartdms.operation_service.dto.Vehicle;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleResponse {

    private Long id;
    private String vehicleNo;
    private String vehicleType;
    private String model;
    private String status;
    private LocalDateTime createdAt;
}