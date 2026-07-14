package com.smartdms.operation_service.dto.saleskpi;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SalesKpiDayResponse {

    private LocalDate date;
    private String dayOfWeek;
    private int plannedCount;
    private int visitedCount;
    private double completionRate;
    private List<SalesKpiStopResponse> stops;
}
