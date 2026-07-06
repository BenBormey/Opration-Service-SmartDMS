package com.smartdms.operation_service.dto.Collection;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CollectionResponse {

    private Long id;

    private Long invoiceId;
    private String invoiceNo;

    private Long customerId;
    private String customerName;

    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime collectionDate;

    private Long collectedBy;
    private String collectedByName;

    // invoice snapshot after this collection
    private BigDecimal invoiceTotal;
    private BigDecimal invoicePaid;
    private BigDecimal invoiceBalance;
    private String invoiceStatus;
}