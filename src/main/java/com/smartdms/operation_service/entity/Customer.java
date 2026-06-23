package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers", schema = "dms")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_name")
    private String customerName;

    private String phone;

    private String address;

    @Column(name = "salesman_id")
    private Long salesmanId;

    @Column(name = "sd_id")
    private Long sdId;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}