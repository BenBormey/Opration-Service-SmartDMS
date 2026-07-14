package com.smartdms.operation_service.dto.customercheckin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCheckinResponse {

    private Long id;

    private Long salesmanId;

    private String salesmanName;

    private Long customerId;

    private String customerName;

    private LocalDateTime checkinTime;

    private Double latitude;

    private Double longitude;

}