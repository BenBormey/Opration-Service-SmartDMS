package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.sd.SdRequest;
import com.smartdms.operation_service.dto.sd.SdResponse;
import com.smartdms.operation_service.service.SdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sds")
@RequiredArgsConstructor
public class SdController {

    private final SdService service;

    @PostMapping
    public SdResponse create(@RequestBody SdRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public SdResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<SdResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public SdResponse update(
            @PathVariable Long id,
            @RequestBody SdRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "SD deleted successfully";
    }
}