package com.smartdms.operation_service.dto.salesorder;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SalesOrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}