package com.smartdms.operation_service.service;



import com.smartdms.operation_service.dto.Product.ProductRequest;
import com.smartdms.operation_service.dto.Product.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}