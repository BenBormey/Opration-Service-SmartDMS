package com.smartdms.operation_service.dto.supplier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    @NotNull(message = "Status is required")
    private Boolean isActive;
}