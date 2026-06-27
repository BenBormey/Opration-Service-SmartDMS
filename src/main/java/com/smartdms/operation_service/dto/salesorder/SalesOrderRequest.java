package com.smartdms.operation_service.dto.salesorder;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalesOrderRequest {

    private Long customerId;
    private Long salesmanId;
    private Long sdId;
    private LocalDate orderDate;
    private String note;
    private List<CreateSalesOrderItemRequest> items;
}