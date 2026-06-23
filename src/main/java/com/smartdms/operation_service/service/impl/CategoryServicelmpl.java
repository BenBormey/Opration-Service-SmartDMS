package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Category.CategoryRequest;
import com.smartdms.operation_service.dto.Category.CategoryResponse;
import com.smartdms.operation_service.entity.Supplier;
import com.smartdms.operation_service.entity.categories;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CategoryRepository;
import com.smartdms.operation_service.service.CategoryService;
import jdk.jfr.Category;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServicelmpl implements CategoryService {

    private  final CategoryRepository categoryRepository;

    public CategoryServicelmpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryResponse create(CategoryRequest request) {

        categories category = new categories();

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setIsActive(request.getIsActive());

        categories saved = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(saved.getId())
                .categoryName(saved.getCategoryName())
                .description(saved.getDescription())
                .isActive(saved.getIsActive())
                .build();
    }

    @Override
    public CategoryResponse getById(Long id) {

        categories category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());
        response.setIsActive(category.getIsActive());

        return response;
    }

    @Override
    public List<CategoryResponse> getAll() {

        List<categories> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }

        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .categoryName(category.getCategoryName())
                        .description(category.getDescription())
                        .isActive(category.getIsActive())
                        .build())
                .toList();
    }
    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        categories category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setIsActive(request.getIsActive());

        categories updated = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(updated.getId())
                .categoryName(updated.getCategoryName())
                .description(updated.getDescription())
                .isActive(updated.getIsActive())
                .build();
    }

    @Override
    public void delete(Long id) {

        categories category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}
