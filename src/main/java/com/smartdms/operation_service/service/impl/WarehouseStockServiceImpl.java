package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.stock.WarehouseStockRequest;
import com.smartdms.operation_service.dto.stock.WarehouseStockResponse;
import com.smartdms.operation_service.entity.WarehouseStock;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.WarehouseStockRepository;
import com.smartdms.operation_service.service.WarehouseStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseStockServiceImpl implements WarehouseStockService {

    private final WarehouseStockRepository repository;

    public WarehouseStockServiceImpl(WarehouseStockRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<WarehouseStockResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public WarehouseStockResponse getById(Long id) {
        WarehouseStock ws = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse stock not found with Id : " + id));
        return toResponse(ws);
    }

    @Override
    @Transactional
    public WarehouseStockResponse create(WarehouseStockRequest request) {
        WarehouseStock ws = WarehouseStock.builder()
                .warehouseId(request.getWarehouseId())
                .productId(request.getProductId())
                .qtyOnHand(request.getQtyOnHand())
                .build();
        return toResponse(repository.save(ws));
    }

    @Override
    @Transactional
    public WarehouseStockResponse update(Long id, WarehouseStockRequest request) {
        WarehouseStock ws = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse stock not found with Id : " + id));
        ws.setWarehouseId(request.getWarehouseId());
        ws.setProductId(request.getProductId());
        ws.setQtyOnHand(request.getQtyOnHand());
        return toResponse(repository.save(ws));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        WarehouseStock ws = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse stock not found with Id : " + id));
        repository.delete(ws);
    }

    private WarehouseStockResponse toResponse(WarehouseStock w) {
        return WarehouseStockResponse.builder()
                .id(w.getId())
                .warehouseId(w.getWarehouseId())
                .productId(w.getProductId())
                .qtyOnHand(w.getQtyOnHand())
                .build();
    }
}