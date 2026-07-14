package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.routeplan.RoutePlanRequest;
import com.smartdms.operation_service.dto.routeplan.RoutePlanResponse;

import java.util.List;

public interface RoutePlanService  {

    RoutePlanResponse create(RoutePlanRequest request);

    RoutePlanResponse update(Long id, RoutePlanRequest request);

    RoutePlanResponse getById(Long id);

    List<RoutePlanResponse> getAll();

    List<RoutePlanResponse> getBySalesmanId(Long salesmanId);

    void delete(Long id);
}
