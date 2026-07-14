package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.category.CategoryRequest;
import com.smartdms.operation_service.dto.category.CategoryResponse;
import com.smartdms.operation_service.entity.Category;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CategoryRepository;
import com.smartdms.operation_service.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {

        Category category = new Category();

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setIsDeleted(false);

        Category saved = categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(saved.getId())
                .categoryName(saved.getCategoryName())
                .description(saved.getDescription())
                .build();
    }

    @Override
    public CategoryResponse getById(Long id) {

        Category category = categoryRepository.findById(id)
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

        List<Category> categoriesList = categoryRepository.findAll();

        if (categoriesList.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }

        return categoriesList.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);

        return toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        // Soft Delete
        category.setIsDeleted(true);
        categoryRepository.save(category);

        // Hard Delete (if needed)
        // categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }
}
