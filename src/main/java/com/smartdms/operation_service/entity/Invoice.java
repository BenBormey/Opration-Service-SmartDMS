//package com.smartdms.operation_service.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "invoices", schema = "dms")
//@Getter
//@Setter
//public class Invoice {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "invoice_no")
//    private String invoiceNo;
//
//    @ManyToOne
//    @JoinColumn(name = "sales_order_id")
//    private SalesOrder salesOrder;
//
//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
//
//    @Column(name = "invoice_date")
//    private LocalDateTime invoiceDate;
//
//    @Column(name = "total_amount")
//    private BigDecimal totalAmount;
//
//    @Column(name = "paid_amount")
//    private BigDecimal paidAmount;
//
//    private String status;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//}