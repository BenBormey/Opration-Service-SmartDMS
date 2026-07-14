package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.driver.DriverStopResponse;
import com.smartdms.operation_service.dto.driver.DriverSummaryResponse;
import com.smartdms.operation_service.dto.driver.DriverVehicleInfoResponse;

import java.time.LocalDate;
import java.util.List;

public interface DriverService {

    List<DriverStopResponse> getStops(Long driverId, LocalDate date);

    DriverSummaryResponse getSummary(Long driverId, LocalDate date);
    DriverVehicleInfoResponse getVehicleInfo(Long driverId);
}