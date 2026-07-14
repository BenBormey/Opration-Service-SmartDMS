package com.smartdms.operation_service.dto.routeplan;

import lombok.Data;

import java.util.List;

@Data
public class RoutePlanRequest {

    private Long salesmanId;

    private String description;

    private Boolean isActive;

    private List<RoutePlanDetailRequest> details;
}