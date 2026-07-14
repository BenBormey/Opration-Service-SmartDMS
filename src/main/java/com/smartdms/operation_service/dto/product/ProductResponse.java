package com.smartdms.operation_service.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String barcode;

    private String productName;

    private Long categoryId;

    private Long supplierId;
    private String supplierName;

    private String unit;

    private BigDecimal buyingPrice;

    private BigDecimal sellingPrice;

    private BigDecimal wholesalePrice;

    private Integer reorderLevel;

    private Boolean isActive;

    private Boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}