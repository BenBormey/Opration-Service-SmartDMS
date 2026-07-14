package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.product.ProductRequest;
import com.smartdms.operation_service.dto.product.ProductResponse;
import com.smartdms.operation_service.entity.Product;
import com.smartdms.operation_service.entity.Supplier;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.ProductRepository;
import com.smartdms.operation_service.repository.SupplierRepository;
import com.smartdms.operation_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final SupplierRepository supplierRepository;

    @Override
    public ProductResponse create(ProductRequest request) {

        if (repository.existsByBarcode(request.getBarcode())) {
            throw new ResourceAlreadyExistsException(
                    "Product barcode already exists: " + request.getBarcode());
        }

        Product product = new Product();
        product.setBarcode(request.getBarcode());
        product.setProductName(request.getProductName());
        product.setCategoryId(request.getCategoryId());
        product.setUnit(request.getUnit());
        product.setBuyingPrice(request.getBuyingPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setIsActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE);
        product.setSupplierId(request.getSupplierId());
        product.setWholesalePrice(request.getWholesalePrice());
        product.setReorderLevel(request.getReorderLevel());
        product.setIsDeleted(false);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = repository.save(product);

        return mapToResponse(saved);
    }

    @Override
    public ProductResponse getById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAll() {

        List<Product> products = repository.findByIsDeletedFalse();

        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        repository.findByBarcode(request.getBarcode())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ResourceAlreadyExistsException(
                            "Product barcode already exists: " + request.getBarcode());
                });

        product.setBarcode(request.getBarcode());
        product.setProductName(request.getProductName());
        product.setCategoryId(request.getCategoryId());
        product.setUnit(request.getUnit());
        product.setBuyingPrice(request.getBuyingPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setIsActive(request.getIsActive());
        product.setSupplierId(request.getSupplierId());
        product.setWholesalePrice(request.getWholesalePrice());
        product.setReorderLevel(request.getReorderLevel());
        product.setUpdatedAt(LocalDateTime.now());

        Product updated = repository.save(product);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setIsDeleted(true);
        product.setUpdatedAt(LocalDateTime.now());

        repository.save(product);
    }

    private ProductResponse mapToResponse(Product product) {

        String supplierName = null;
        if (product.getSupplierId() != null) {
            supplierName = supplierRepository.findById(product.getSupplierId())
                    .map(Supplier::getSupplierName)
                    .orElse(null);
        }

        return ProductResponse.builder()
                .id(product.getId())
                .barcode(product.getBarcode())
                .productName(product.getProductName())
                .categoryId(product.getCategoryId())
                .unit(product.getUnit())
                .buyingPrice(product.getBuyingPrice())
                .sellingPrice(product.getSellingPrice())
                .isActive(product.getIsActive())
                .supplierId(product.getSupplierId())
                .supplierName(supplierName)
                .wholesalePrice(product.getWholesalePrice())
                .reorderLevel(product.getReorderLevel())
                .isDeleted(product.getIsDeleted())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}