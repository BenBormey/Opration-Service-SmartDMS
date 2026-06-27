package com.smartdms.operation_service.dto.salesorder;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateSalesOrderItemRequest {

    private Long productId;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    // lineTotal computed server-side
}