package com.smartdms.operation_service.dto.customercheckin;

import lombok.Data;

@Data
public class CustomerCheckinRequest {

    private Long salesmanId;
    private Long customerId;
    private Double latitude;
    private Double longitude;
}