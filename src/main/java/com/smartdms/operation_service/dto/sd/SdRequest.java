package com.smartdms.operation_service.dto.sd;

import lombok.Data;

@Data
public class SdRequest {

    private String sdCode;

    private String sdName;

    private String phone;

    private String address;

    private Boolean isActive;
}