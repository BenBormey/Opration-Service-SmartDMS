package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "collections", schema = "dms")
@Data
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceId;

    private Long customerId;

    private BigDecimal amount;

    private String paymentMethod;

    private LocalDateTime collectionDate;

    private Long collectedBy;
}