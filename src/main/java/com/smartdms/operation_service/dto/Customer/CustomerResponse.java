package com.smartdms.operation_service.dto.Customer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CustomerResponse {

    private Long id;
    private String customerCode;
    private String customerName;
    private String phone;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long salesmanId;
    private Long sdId;
    private BigDecimal creditLimit;
    private BigDecimal balanceDue;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}