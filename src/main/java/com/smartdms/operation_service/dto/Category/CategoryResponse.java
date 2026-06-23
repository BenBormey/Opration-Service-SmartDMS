package com.smartdms.operation_service.dto.Category;

import lombok.*;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;
    private String categoryName;
    private String description;
}