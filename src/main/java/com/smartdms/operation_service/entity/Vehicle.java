package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_no", nullable = false, unique = true)
    private String vehicleNo;

    @Column(name = "vehicle_type")
    private String vehicleType;

    private String model;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}