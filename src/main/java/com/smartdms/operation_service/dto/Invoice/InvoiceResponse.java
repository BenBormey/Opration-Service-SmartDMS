package com.smartdms.operation_service.dto.Invoice;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceResponse {

    private Long id;
    private String invoiceNo;

    private Long salesOrderId;

    private Long customerId;
    private String customerName;

    private LocalDateTime invoiceDate;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceDue;   // computed: total - paid
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}