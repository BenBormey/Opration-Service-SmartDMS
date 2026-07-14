package com.smartdms.operation_service.dto.routeplan;

import lombok.Data;

@Data
public class RoutePlanDetailRequest {

    private Long customerId;

    private String dayOfWeek;

    private Integer sequenceNo;
}