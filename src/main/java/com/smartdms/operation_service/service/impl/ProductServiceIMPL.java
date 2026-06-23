package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Product.ProductRequest;
import com.smartdms.operation_service.dto.Product.ProductResponse;
import com.smartdms.operation_service.entity.Product;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.ProductRepository;
import com.smartdms.operation_service.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceIMPL implements ProductService {


    private  final ProductRepository repository;

    public ProductServiceIMPL(ProductRepository repository) {
        this.repository = repository;
    }

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
        product.setIsActive(request.getIsActive());
        product.setSupplierId(request.getSupplierId());

        Product saved = repository.save(product);

        return ProductResponse.builder()
                .id(saved.getId())
                .barcode(saved.getBarcode())
                .productName(saved.getProductName())
                .categoryId(saved.getCategoryId())
                .unit(saved.getUnit())
                .buyingPrice(saved.getBuyingPrice())
                .sellingPrice(saved.getSellingPrice())
                .isActive(saved.getIsActive())
                .createdAt(saved.getCreatedAt())
                .supplierId(saved.getSupplierId())
                .build();
    }

    @Override
    public ProductResponse getById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        return ProductResponse.builder()
                .id(product.getId())
                .barcode(product.getBarcode())
                .productName(product.getProductName())
                .categoryId(product.getCategoryId())
                .unit(product.getUnit())
                .buyingPrice(product.getBuyingPrice())
                .sellingPrice(product.getSellingPrice())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .supplierId(product.getSupplierId())
                .build();
    }

    @Override
    public List<ProductResponse> getAll() {

        List<Product> products = repository.findAll();

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .barcode(product.getBarcode())
                        .productName(product.getProductName())
                        .categoryId(product.getCategoryId())
                        .unit(product.getUnit())
                        .buyingPrice(product.getBuyingPrice())
                        .sellingPrice(product.getSellingPrice())
                        .isActive(product.getIsActive())
                        .createdAt(product.getCreatedAt())
                        .supplierId(product.getSupplierId())
                        .build())
                .toList();
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        product.setBarcode(request.getBarcode());
        product.setProductName(request.getProductName());
        product.setCategoryId(request.getCategoryId());
        product.setUnit(request.getUnit());
        product.setBuyingPrice(request.getBuyingPrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setIsActive(request.getIsActive());
        product.setSupplierId(request.getSupplierId());

        Product updated = repository.save(product);

        return ProductResponse.builder()
                .id(updated.getId())
                .barcode(updated.getBarcode())
                .productName(updated.getProductName())
                .categoryId(updated.getCategoryId())
                .unit(updated.getUnit())
                .buyingPrice(updated.getBuyingPrice())
                .sellingPrice(updated.getSellingPrice())
                .isActive(updated.getIsActive())
                .createdAt(updated.getCreatedAt())
                .supplierId(updated.getSupplierId())
                .build();
    }

    @Override
    public void delete(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id));

        repository.delete(product);
    }
}
