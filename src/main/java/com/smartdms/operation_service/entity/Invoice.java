package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices", schema = "dms")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNo;

    private Long salesOrderId;

    private Long customerId;

    private LocalDateTime invoiceDate;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private String status; // e.g. UNPAID / PARTIAL / PAID

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}