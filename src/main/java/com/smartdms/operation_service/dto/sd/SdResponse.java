package com.smartdms.operation_service.dto.sd;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Data
public class SdResponse {

    private Long id;

    private String sdCode;

    private String sdName;

    private String phone;

    private String address;

    private Boolean isActive;

    private LocalDateTime createdAt;
}