package com.smartdms.operation_service.dto.Product;


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

    private String unit;

    private BigDecimal buyingPrice;

    private BigDecimal sellingPrice;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private Long supplierId;
}