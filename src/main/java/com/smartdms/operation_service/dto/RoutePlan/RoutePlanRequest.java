package com.smartdms.operation_service.dto.RoutePlan;


import lombok.Data;

import java.time.LocalDate;

@Data
public class RoutePlanRequest {

    private Long salesmanId;
    private LocalDate planDate;
    private String description;
    private String dayOfWeek;
}