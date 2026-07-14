package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.saleskpi.SalesKpiResponse;

import java.time.LocalDate;

public interface SalesKpiService {

    SalesKpiResponse getKpi(Long salesmanId, LocalDate startDate, LocalDate endDate);
}
