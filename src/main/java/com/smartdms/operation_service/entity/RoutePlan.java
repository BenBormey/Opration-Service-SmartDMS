package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "route_plans")
@Data
public class RoutePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salesman_id", nullable = false)
    private Long salesmanId;

    @Column(name = "plan_date")
    private LocalDate planDate;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "day_of_week")
    private String dayOfWeek;
}