package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_ledgers", schema = "dms")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StockLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sd_id")
    private Long sdId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "trx_type")
    private String trxType;

    @Column(name = "qty_in")
    private Integer qtyIn;

    @Column(name = "qty_out")
    private Integer qtyOut;

    @Column(name = "balance_qty")
    private Integer balanceQty;

    @Column(name = "trx_date")
    private LocalDateTime trxDate;
}