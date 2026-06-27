package com.smartdms.operation_service.dto.TransferOrder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@Data
public class TransferOrderRequest {

    private String transferNo;

    private Long fromWarehouseId;
    private Long toWarehouseId;

    private LocalDateTime transferDate;

    private String status;
    private LocalDateTime createdAt;
    private List<TransferOrderItemRequest> items;
}