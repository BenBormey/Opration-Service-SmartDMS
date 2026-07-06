package com.smartdms.operation_service.dto.Invoice;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceRequest {

    private String invoiceNo;
    private Long salesOrderId;
    private Long customerId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String status;
}