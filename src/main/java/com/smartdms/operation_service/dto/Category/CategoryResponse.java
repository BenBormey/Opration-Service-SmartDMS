package com.smartdms.operation_service.dto.Category;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String categoryName;
    private String description;
    private Boolean isActive;
}