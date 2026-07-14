package com.smartdms.operation_service.dto.collection;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CollectionRequest {

    private Long invoiceId;
    private Long customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private Long collectedBy;
}