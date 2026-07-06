package com.smartdms.operation_service.dto.Product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    private String barcode;
    private Boolean isDeleted;
    private String productName;

    private Long categoryId;

    private String unit;

    private BigDecimal buyingPrice;

    private BigDecimal sellingPrice;
    private BigDecimal wholesalePrice;
    private Integer reorderLevel;


    private Boolean isActive;

    private Long supplierId;

}