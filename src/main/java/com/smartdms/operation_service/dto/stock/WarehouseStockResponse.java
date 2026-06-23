package com.smartdms.operation_service.dto.stock;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseStockResponse {
    private Long id;
    private Long warehouseId;
    private Long productId;
    private Integer qtyOnHand;
}