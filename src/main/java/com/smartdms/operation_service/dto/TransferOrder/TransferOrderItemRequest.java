package com.smartdms.operation_service.dto.TransferOrder;

import lombok.Data;

@Data
public class TransferOrderItemRequest {

    private Long productId;
    private String productName;
    private String barcode;
    private Double qty;
}