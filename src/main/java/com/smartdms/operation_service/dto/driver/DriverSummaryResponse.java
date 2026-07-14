package com.smartdms.operation_service.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverSummaryResponse {
    private long totalStops;
    private long completed;
    private long inProgress;
    private long pending;
    private int progressPercent;
}