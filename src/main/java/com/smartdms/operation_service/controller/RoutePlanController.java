package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.RoutePlan.RoutePlanRequest;
import com.smartdms.operation_service.dto.RoutePlan.RoutePlanResponse;
import com.smartdms.operation_service.service.RoutePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route-plans")
@RequiredArgsConstructor
public class RoutePlanController {

    private final RoutePlanService routePlanService;

    @PostMapping
    public ResponseEntity<RoutePlanResponse> create(
            @RequestBody RoutePlanRequest request) {

        RoutePlanResponse response = routePlanService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutePlanResponse> update(
            @PathVariable Long id,
            @RequestBody RoutePlanRequest request) {

        RoutePlanResponse response = routePlanService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutePlanResponse> getById(
            @PathVariable Long id) {

        RoutePlanResponse response = routePlanService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RoutePlanResponse>> getAll() {

        List<RoutePlanResponse> responses = routePlanService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/salesman/{salesmanId}")
    public ResponseEntity<List<RoutePlanResponse>> getBySalesmanId(
            @PathVariable Long salesmanId) {

        List<RoutePlanResponse> responses =
                routePlanService.getBySalesmanId(salesmanId);

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        routePlanService.delete(id);

        return ResponseEntity.ok("Route Plan deleted successfully.");
    }
}