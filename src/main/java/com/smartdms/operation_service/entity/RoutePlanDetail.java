package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "route_plan_details", schema = "dms")
@Data
public class RoutePlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String dayOfWeek;

    private Integer sequenceNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_plan_id")
    private RoutePlan routePlan;
}