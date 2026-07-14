package com.smartdms.operation_service.dto.transferorder;

import lombok.Data;

@Data
public class TransferOrderItemRequest {

    private Long productId;
    private Double qty;
}
