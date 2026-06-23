package com.smartdms.operation_service.dto.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String description;

    private Boolean isActive;
}
