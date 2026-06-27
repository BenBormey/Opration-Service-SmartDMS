package com.smartdms.operation_service.dto.TransferOrder;

import lombok.Data;

@Data
public class TransferOrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String barcode;
    private Double qty;
}