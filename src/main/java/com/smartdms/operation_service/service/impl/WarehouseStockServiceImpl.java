package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.stock.WarehouseStockRequest;
import com.smartdms.operation_service.dto.stock.WarehouseStockResponse;
import com.smartdms.operation_service.entity.Product;
import com.smartdms.operation_service.entity.Warehouse;
import com.smartdms.operation_service.entity.WarehouseStock;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.ProductRepository;
import com.smartdms.operation_service.repository.WarehouseRepository;
import com.smartdms.operation_service.repository.WarehouseStockRepository;
import com.smartdms.operation_service.service.WarehouseStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarehouseStockServiceImpl implements WarehouseStockService {

    private final WarehouseStockRepository repository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    public WarehouseStockServiceImpl(
            WarehouseStockRepository repository,
            WarehouseRepository warehouseRepository,
            ProductRepository productRepository) {

        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<WarehouseStockResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public WarehouseStockResponse getById(Long id) {
        WarehouseStock ws = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse stock not found with Id : " + id));

        return toResponse(ws);
    }

    @Override
    @Transactional
    public WarehouseStockResponse create(WarehouseStockRequest request) {

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found with Id : " + request.getWarehouseId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with Id : " + request.getProductId()));

        WarehouseStock ws = WarehouseStock.builder()
                .warehouse(warehouse)
                .product(product)
                .qtyOnHand(request.getQtyOnHand())
                .updatedAt(LocalDateTime.now())
                .build();

        ws = repository.save(ws);

        return toResponse(ws);
    }

    @Override
    @Transactional
    public WarehouseStockResponse update(Long id, WarehouseStockRequest request) {

        WarehouseStock ws = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse stock not found with Id : " + id));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found with Id : " + request.getWarehouseId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with Id : " + request.getProductId()));

        ws.setWarehouse(warehouse);
        ws.setProduct(product);
        ws.setQtyOnHand(request.getQtyOnHand());

        ws = repository.save(ws);

        return toResponse(ws);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        WarehouseStock ws = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse stock not found with Id : " + id));

        repository.delete(ws);
    }

    private WarehouseStockResponse toResponse(WarehouseStock stock) {

        return WarehouseStockResponse.builder()
                .id(stock.getId())
                .warehouseId(stock.getWarehouse().getId())
                .warehouseName(stock.getWarehouse().getWarehouseName())
                .productId(stock.getProduct().getId())
                .productName(stock.getProduct().getProductName())
                .qtyOnHand(stock.getQtyOnHand())
                .build();
    }
}