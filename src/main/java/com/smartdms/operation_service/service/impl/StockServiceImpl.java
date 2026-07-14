package com.smartdms.operation_service.service.impl;


import com.smartdms.operation_service.dto.stock.StockRequest;
import com.smartdms.operation_service.dto.stock.StockResponse;
import com.smartdms.operation_service.entity.Product;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.entity.Stock;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.ProductRepository;
import com.smartdms.operation_service.repository.StockRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final ProductRepository productRepository;
    private final StockRepository repository;
    private final UserRepository userRepository;
    @Override
    public StockResponse create(StockRequest request) {

        User user = userRepository.findById(request.getSdId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "SD not found with id: " + request.getSdId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + request.getProductId()));

        if (repository.findBySdIdAndProductId(request.getSdId(), request.getProductId()).isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Stock already exists for sdId=" + request.getSdId()
                            + " and productId=" + request.getProductId());
        }

        Stock stock = new Stock();
        stock.setSdId(user.getId());
        stock.setProductId(product.getId());
        stock.setQtyOnHand(request.getQtyOnHand());

        Stock saved = repository.save(stock);

        return mapToResponse(saved);
    }

    @Override
    public StockResponse getById(Long id) {

        Stock stock = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Stock not found with id: " + id));

        return mapToResponse(stock);
    }

    @Override
    public List<StockResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public StockResponse update(Long id, StockRequest request) {

        Stock stock = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Stock not found with id: " + id));
        User user = userRepository.findById(request.getSdId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "SD not found with id: " + request.getSdId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + request.getProductId()));

        repository.findBySdIdAndProductId(request.getSdId(), request.getProductId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResourceAlreadyExistsException(
                            "Stock already exists for sdId=" + request.getSdId()
                                    + " and productId=" + request.getProductId());
                });

        stock.setSdId(request.getSdId());
        stock.setProductId(request.getProductId());
        stock.setQtyOnHand(request.getQtyOnHand());

        Stock updated = repository.save(stock);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Stock stock = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Stock not found with id: " + id));

        repository.delete(stock);
    }

    private StockResponse mapToResponse(Stock stock) {


        User user  = userRepository.findById(stock.getSdId())
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));
        Product product = productRepository.findById(stock.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return StockResponse.builder()
                .id(stock.getId())
                .sdId(stock.getSdId())
                .sdName(user.getFullName())
                .productId(stock.getProductId())
                .productName(product.getProductName())
                .qtyOnHand(stock.getQtyOnHand())
                .build();
    }
}