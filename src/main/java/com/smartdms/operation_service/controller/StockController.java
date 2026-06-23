package com.smartdms.operation_service.controller;

import com.smartdms.operation_service.dto.stock.StockRequest;
import com.smartdms.operation_service.dto.stock.StockResponse;
import com.smartdms.operation_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public StockResponse create(@RequestBody StockRequest request) {
        return stockService.create(request);
    }

    @GetMapping("/{id}")
    public StockResponse getById(@PathVariable Long id) {
        return stockService.getById(id);
    }

    @GetMapping
    public List<StockResponse> getAll() {
        return stockService.getAll();
    }

    @PutMapping("/{id}")
    public StockResponse update(
            @PathVariable Long id,
            @RequestBody StockRequest request) {

        return stockService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        stockService.delete(id);
        return "Stock deleted successfully";
    }
}