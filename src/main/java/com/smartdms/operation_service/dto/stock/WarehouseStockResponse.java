package com.smartdms.operation_service.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStockResponse {

    private Long id;

    private Long warehouseId;
    private String warehouseName;

    private Long productId;
    private String productName;

    private Integer qtyOnHand;
}