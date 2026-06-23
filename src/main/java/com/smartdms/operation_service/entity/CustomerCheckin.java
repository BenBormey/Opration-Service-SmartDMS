package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_checkins", schema = "dms")
@Data
@Builder @NoArgsConstructor
@AllArgsConstructor
public class CustomerCheckin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesman_id")
    private User salesman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "checkin_time")
    private LocalDateTime checkinTime;

    private Double latitude;
    private Double longitude;
}