package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.Driver.DriverStopResponse;
import com.smartdms.operation_service.dto.Driver.DriverSummaryResponse;
import com.smartdms.operation_service.dto.Driver.DriverVehicleInfoResponse;

import java.time.LocalDate;
import java.util.List;

public interface DriverService {

    List<DriverStopResponse> getStops(Long driverId, LocalDate date);

    DriverSummaryResponse getSummary(Long driverId, LocalDate date);
    DriverVehicleInfoResponse getVehicleInfo(Long driverId);
}