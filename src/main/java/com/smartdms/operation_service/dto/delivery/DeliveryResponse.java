package com.smartdms.operation_service.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private Long id;
    private String deliveryNo;
    private Long invoiceId;
    private String invoiceNo;
    private Long customerId;
    private String customerName;
    private Long driverId;
    private Long vehicleId;
    private String vehicleNo;
    private Long routePlanId;
    private Long driverAssignmentId;
    private LocalDate deliveryDate;
    private String fullName;

    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime arrivedAt;
    private LocalDateTime completedAt;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}