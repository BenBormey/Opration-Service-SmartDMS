package com.smartdms.operation_service.dto.Customer;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CustomerRequest {
    private String customerCode;

    private String customerName;

    private String phone;

    private String address;

    private Long salesmanId;

    private Long sdId;

    private BigDecimal creditLimit;

    private Boolean isActive;
}
