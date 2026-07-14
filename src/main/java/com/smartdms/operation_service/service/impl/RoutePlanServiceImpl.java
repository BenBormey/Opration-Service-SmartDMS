package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.routeplan.RoutePlanDetailRequest;
import com.smartdms.operation_service.dto.routeplan.RoutePlanDetailResponse;
import com.smartdms.operation_service.dto.routeplan.RoutePlanRequest;
import com.smartdms.operation_service.dto.routeplan.RoutePlanResponse;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.entity.RoutePlan;
import com.smartdms.operation_service.entity.RoutePlanDetail;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.repository.RoutePlanRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.RoutePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutePlanServiceImpl implements RoutePlanService {

    private final RoutePlanRepository routePlanRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Override
    public RoutePlanResponse create(RoutePlanRequest request) {

        // ប្រសិនបើ salesman នេះ មាន route plan រួចហើយ — កុំឱ្យ add ម្ដងទៀត
        if (routePlanRepository.existsBySalesmanId(request.getSalesmanId())) {
            throw new ResourceAlreadyExistsException(
                    "Route Plan already exists for salesman id: " + request.getSalesmanId());
        }

        RoutePlan routePlan = new RoutePlan();
        routePlan.setSalesmanId(request.getSalesmanId());
        routePlan.setDescription(request.getDescription());
        routePlan.setIsActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE);
        routePlan.setCreatedAt(LocalDateTime.now());

        applyDetails(routePlan, request.getDetails());

        routePlan = routePlanRepository.save(routePlan);

        return mapToResponse(routePlan);
    }

    @Override
    public RoutePlanResponse update(Long id, RoutePlanRequest request) {

        RoutePlan routePlan = routePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route Plan not found with id: " + id));

        boolean salesmanTaken = routePlanRepository.findBySalesmanId(request.getSalesmanId())
                .stream()
                .anyMatch(existing -> !existing.getId().equals(id));
        if (salesmanTaken) {
            throw new ResourceAlreadyExistsException(
                    "Route Plan already exists for salesman id: " + request.getSalesmanId());
        }

        routePlan.setSalesmanId(request.getSalesmanId());
        routePlan.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            routePlan.setIsActive(request.getIsActive());
        }

        // Replace all detail lines (orphanRemoval deletes the old ones)
        routePlan.getDetails().clear();
        applyDetails(routePlan, request.getDetails());

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

        routePlan.setIsActive(false);

        routePlanRepository.save(routePlan);
    }

    private void applyDetails(RoutePlan routePlan, List<RoutePlanDetailRequest> detailRequests) {

        if (detailRequests == null) {
            return;
        }

        for (RoutePlanDetailRequest d : detailRequests) {
            RoutePlanDetail detail = new RoutePlanDetail();
            detail.setCustomerId(d.getCustomerId());
            detail.setDayOfWeek(d.getDayOfWeek());
            detail.setSequenceNo(d.getSequenceNo());
            detail.setRoutePlan(routePlan); // keep both sides of the relationship in sync
            routePlan.getDetails().add(detail);
        }
    }

    private RoutePlanResponse mapToResponse(RoutePlan routePlan) {

        RoutePlanResponse response = new RoutePlanResponse();

        response.setId(routePlan.getId());
        response.setSalesmanId(routePlan.getSalesmanId());
        response.setDescription(routePlan.getDescription());
        response.setIsActive(routePlan.getIsActive());
        response.setCreatedAt(routePlan.getCreatedAt());

        User salesman = userRepository.findById(routePlan.getSalesmanId())
                .orElse(null);
        if (salesman != null) {
            response.setSalesmanName(salesman.getFullName());
        }

        List<RoutePlanDetailResponse> detailResponses = routePlan.getDetails() == null
                ? new ArrayList<>()
                : routePlan.getDetails().stream()
                .map(this::mapDetailToResponse)
                .collect(Collectors.toList());
        response.setDetails(detailResponses);

        return response;
    }

    private RoutePlanDetailResponse mapDetailToResponse(RoutePlanDetail detail) {

        RoutePlanDetailResponse response = new RoutePlanDetailResponse();

        response.setId(detail.getId());
        response.setCustomerId(detail.getCustomerId());
        response.setDayOfWeek(detail.getDayOfWeek());
        response.setSequenceNo(detail.getSequenceNo());

        Customer customer = customerRepository.findById(detail.getCustomerId())
                .orElse(null);
        if (customer != null) {
            response.setCustomerName(customer.getCustomerName());
        }

        return response;
    }
}