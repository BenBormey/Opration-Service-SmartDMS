package com.smartdms.operation_service.dto.saleskpi;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SalesKpiResponse {

    private Long salesmanId;
    private String salesmanName;
    private LocalDate startDate;
    private LocalDate endDate;

    private int plannedVisits;
    private int completedVisits;
    private double completionRate;
    private int extraVisits;

    private List<SalesKpiDayResponse> days;
}
