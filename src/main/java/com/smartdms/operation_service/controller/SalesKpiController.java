package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.saleskpi.SalesKpiResponse;
import com.smartdms.operation_service.service.SalesKpiService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales-kpi")
@RequiredArgsConstructor
public class SalesKpiController {

    private final SalesKpiService salesKpiService;

    // GET /api/sales-kpi?salesmanId=1                         -> today only
    // GET /api/sales-kpi?salesmanId=1&startDate=&endDate=      -> range (e.g. the week)
    @GetMapping
    public ResponseEntity<SalesKpiResponse> getKpi(
            @RequestParam Long salesmanId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDate end = endDate != null ? endDate : LocalDate.now();
        LocalDate start = startDate != null ? startDate : end;

        return ResponseEntity.ok(salesKpiService.getKpi(salesmanId, start, end));
    }
}
