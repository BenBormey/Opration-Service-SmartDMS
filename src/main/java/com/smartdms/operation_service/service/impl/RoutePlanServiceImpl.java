package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.RoutePlan.RoutePlanRequest;
import com.smartdms.operation_service.dto.RoutePlan.RoutePlanResponse;
import com.smartdms.operation_service.entity.RoutePlan;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.RoutePlanRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.RoutePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutePlanServiceImpl implements RoutePlanService {

    private final RoutePlanRepository routePlanRepository;
    private final UserRepository repository;

    @Override
    public RoutePlanResponse create(RoutePlanRequest request) {

        RoutePlan routePlan = new RoutePlan();
        routePlan.setSalesmanId(request.getSalesmanId());
        routePlan.setPlanDate(request.getPlanDate());
        routePlan.setDescription(request.getDescription());
        routePlan.setDayOfWeek(request.getDayOfWeek());
        routePlan.setCreatedAt(LocalDateTime.now());

        routePlan = routePlanRepository.save(routePlan);

        return mapToResponse(routePlan);
    }

    @Override
    public RoutePlanResponse update(Long id, RoutePlanRequest request) {

        RoutePlan routePlan = routePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route Plan not found with id: " + id));

        routePlan.setSalesmanId(request.getSalesmanId());
        routePlan.setPlanDate(request.getPlanDate());
        routePlan.setDescription(request.getDescription());
        routePlan.setDayOfWeek(request.getDayOfWeek());

        routePlan = routePlanRepository.save(routePlan);

        return mapToResponse(routePlan);
    }

    @Override
    public RoutePlanResponse getById(Long id) {

        RoutePlan routePlan = routePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route Plan not found with id: " + id));

        return mapToResponse(routePlan);
    }

    @Override
    public List<RoutePlanResponse> getAll() {

        return routePlanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoutePlanResponse> getBySalesmanId(Long salesmanId) {

        return routePlanRepository.findBySalesmanId(salesmanId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        RoutePlan routePlan = routePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route Plan not found with id: " + id));

        routePlanRepository.delete(routePlan);
    }

    private RoutePlanResponse mapToResponse(RoutePlan routePlan) {

        RoutePlanResponse response = new RoutePlanResponse();

        response.setId(routePlan.getId());
        response.setSalesmanId(routePlan.getSalesmanId());
        response.setPlanDate(routePlan.getPlanDate());
        response.setDescription(routePlan.getDescription());
        response.setCreatedAt(routePlan.getCreatedAt());
        response.setDayOfWeek(routePlan.getDayOfWeek());

        User salesman = repository.findById(routePlan.getSalesmanId())
                .orElse(null);

        if (salesman != null) {
            response.setSalesmanName(salesman.getFullName()); // ឬ getFullName()
        }

        return response;
    }
}