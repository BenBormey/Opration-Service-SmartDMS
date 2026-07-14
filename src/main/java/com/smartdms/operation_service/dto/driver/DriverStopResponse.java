package com.smartdms.operation_service.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverStopResponse {
    private Integer stopNumber;      // 1, 2, 3...
    private Long transferOrderId;    // ដើម្បីចុច Start/Arrived
    private String storeName;        // customer_name
    private String address;          // address
    private Double latitude;
    private Double longitude;
    private String status;           // "Pending" / "In Progress" / "Completed"
    private String rawStatus;        // REQUEST/APPROVED/SHIPPED/RECEIVED (សម្រាប់ logic)
    private Integer itemCount;       // ចំនួន product lines
    private Integer totalQty;        // sum qty
}