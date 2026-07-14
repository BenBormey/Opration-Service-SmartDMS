package com.smartdms.operation_service.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    private String categoryName;
    private String description;
}