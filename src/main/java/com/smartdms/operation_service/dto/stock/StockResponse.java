package com.smartdms.operation_service.dto.stock;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockResponse {
    private Long id;

    private Long sdId;
    private String sdName;

    private Long productId;
    private String productName;

    private Integer qtyOnHand;
}