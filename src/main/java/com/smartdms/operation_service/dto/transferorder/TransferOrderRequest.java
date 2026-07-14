package com.smartdms.operation_service.dto.transferorder;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransferOrderRequest {

    private String transferNo;

    private Long fromWarehouseId;
    private Long toSdId;

    private Long driverId;
    private Long vehicleId;

    private LocalDateTime transferDate;
    private String remark;

    private List<TransferOrderItemRequest> items;
}
