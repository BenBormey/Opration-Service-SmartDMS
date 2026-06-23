package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Category.CategoryRequest;
import com.smartdms.operation_service.dto.Category.CategoryResponse;
import com.smartdms.operation_service.entity.categories;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CategoryRepository;
import com.smartdms.operation_service.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServicelmpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServicelmpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {

        categories category = new categories();

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setIsDeleted(false);

        categories saved = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(saved.getId())
                .categoryName(saved.getCategoryName())
                .description(saved.getDescription())
                .build();
    }

    @Override
    public CategoryResponse getById(Long id) {

        categories category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public List<CategoryResponse> getAll() {

        List<categories> categoriesList = categoryRepository.findAll();

        if (categoriesList.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }

        return categoriesList.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        categories category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        categories updated = categoryRepository.save(category);

        return toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        categories category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        // Soft Delete
        category.setIsDeleted(true);
        categoryRepository.save(category);

        // Hard Delete (if needed)
        // categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(categories category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }
}