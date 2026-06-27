package com.smartdms.operation_service.dto.Vehicle;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleRequest {

    private String vehicleNo;
    private String vehicleType;
    private String model;
    private String status;
}