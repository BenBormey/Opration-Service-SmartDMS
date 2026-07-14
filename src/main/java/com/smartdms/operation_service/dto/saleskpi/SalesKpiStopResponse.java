package com.smartdms.operation_service.dto.saleskpi;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SalesKpiStopResponse {

    private Long customerId;
    private String customerName;
    private Integer sequenceNo;
    private boolean visited;
    private LocalDateTime checkinTime;
}
