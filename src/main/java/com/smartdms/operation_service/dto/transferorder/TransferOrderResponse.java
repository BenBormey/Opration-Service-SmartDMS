package com.smartdms.operation_service.dto.transferorder;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TransferOrderResponse {

    private Long id;
    private String transferNo;

    private Long fromWarehouseId;
    private Long toSdId;

    private Long driverId;
    private Long vehicleId;

    private LocalDateTime transferDate;
    private String status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String remark;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<TransferOrderItemResponse> items;
}
