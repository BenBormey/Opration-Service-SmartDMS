package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.Category.CategoryResponse;
import com.smartdms.operation_service.dto.Category.CategoryRequest;


import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

    CategoryResponse update(Long id, CategoryRequest request);

    void delete(Long id);
}