package com.smartdms.operation_service.dto.routeplan;

import lombok.Data;

@Data
public class RoutePlanDetailResponse {

    private Long id;

    private Long customerId;
    private String customerName;

    private String dayOfWeek;

    private Integer sequenceNo;
}