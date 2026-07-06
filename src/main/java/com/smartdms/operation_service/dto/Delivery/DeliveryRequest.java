package com.smartdms.operation_service.dto.Delivery;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DeliveryRequest {
    private String deliveryNo;      // optional — auto-generated if empty
    private Long invoiceId;
    private Long customerId;
    private Long driverId;
    private Long vehicleId;
    private Long routePlanId;
    private Long driverAssignmentId;
    private LocalDate deliveryDate;
    private String remark;
}