package com.smartdms.operation_service.dto.Collection;

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