package com.smartdms.operation_service.dto.RoutePlan;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoutePlanResponse {

    private Long id;

    private Long salesmanId;
    private String salesmanName;

    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private List<RoutePlanDetailResponse> details;
}