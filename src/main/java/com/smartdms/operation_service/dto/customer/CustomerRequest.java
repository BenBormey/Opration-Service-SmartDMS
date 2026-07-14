package com.smartdms.operation_service.dto.customer;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CustomerRequest {
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
}
