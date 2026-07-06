//package com.smartdms.operation_service.dto.Delivery;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class DeliveryResponse {
//
//    private Long id;
//
//    private Integer stopNumber;        // "3" in the Next Stop card
//    private String storeName;          // "KH Store Mart"
//    private String address;            // "Street 101, Phnom Penh"
//
//    private Double latitude;           // for navigation on map
//    private Double longitude;
//
//    private Integer packageCount;      // packages for this stop
//    private BigDecimal codAmount;      // COD to collect at this stop
//
//    private String status;             // PENDING / DELIVERED / FAILED
//    private LocalDateTime eta;         // "ETA 10:30 AM"
//    private Double distanceKm;         // "8.5 km away"
//
//    private LocalDateTime deliveredAt; // when it was completed
//}