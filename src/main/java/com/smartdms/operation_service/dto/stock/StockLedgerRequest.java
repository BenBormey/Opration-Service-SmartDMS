package com.smartdms.operation_service.dto.stock;

import lombok.Data;

@Data
public class StockLedgerRequest {
    private Long sdId;
    private Long productId;
    private String trxType;
    private Integer qtyIn;
    private Integer qtyOut;
    private Integer balanceQty;
    private Long warehouseId;
}