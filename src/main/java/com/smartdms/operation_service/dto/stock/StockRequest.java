package com.smartdms.operation_service.dto.stock;


import lombok.Data;

@Data
public class StockRequest {
    private Long sdId;
    private Long productId;
    private Integer qtyOnHand;
}