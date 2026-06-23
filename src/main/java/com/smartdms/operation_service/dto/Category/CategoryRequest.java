package com.smartdms.operation_service.dto.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    private String categoryName;
    private String description;
}