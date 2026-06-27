package com.smartdms.operation_service.dto.salesorder;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SalesOrderResponse {

    private Long id;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private Long salesmanId;
    private String salesmanName;
    private Long sdId;
    private String sdName;
    private LocalDate orderDate;
    private String status;
    private BigDecimal totalAmount;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SalesOrderItemResponse> items;
}