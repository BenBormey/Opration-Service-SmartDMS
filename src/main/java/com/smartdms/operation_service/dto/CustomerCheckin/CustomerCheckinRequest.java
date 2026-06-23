package com.smartdms.operation_service.dto.CustomerCheckin;

import lombok.Data;

@Data
public class CustomerCheckinRequest {

    private Long salesmanId;
    private Long customerId;
    private Double latitude;
    private Double longitude;
}