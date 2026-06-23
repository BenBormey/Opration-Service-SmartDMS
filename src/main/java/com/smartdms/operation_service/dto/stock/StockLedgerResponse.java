package com.smartdms.operation_service.dto.stock;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class StockLedgerResponse {
    private Long id;
    private Long sdId;
    private Long productId;
    private String trxType;
    private Integer qtyIn;
    private Integer qtyOut;
    private Integer balanceQty;
    private LocalDateTime trxDate;
}