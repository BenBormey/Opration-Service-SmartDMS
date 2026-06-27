package com.smartdms.operation_service.dto.RoutePlan;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RoutePlanResponse {

    private Long id;
    private Long salesmanId;
    private String salesmanName;

    private LocalDate planDate;
    private String description;
    private LocalDateTime createdAt;
    private String dayOfWeek;
}