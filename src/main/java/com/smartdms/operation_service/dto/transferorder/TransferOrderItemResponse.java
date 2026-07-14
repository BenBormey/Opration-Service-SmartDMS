package com.smartdms.operation_service.dto.transferorder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferOrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String barcode;
    private Double qty;
}