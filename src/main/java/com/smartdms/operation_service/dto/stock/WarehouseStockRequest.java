package com.smartdms.operation_service.dto.stock;



import lombok.Data;

@Data
public class WarehouseStockRequest {

    private Long warehouseId;
    private Long productId;
    private Integer qtyOnHand;
}