package com.smartdms.operation_service.dto.supplier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierResponse {

    private Long id;
    private String supplierCode;
    private String supplierName;
    private String phone;
    private String address;
    private Boolean isActive;
}