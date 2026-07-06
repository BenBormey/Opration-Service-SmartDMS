package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "driver_assignments")
@Data
public class DriverAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId;

    private Long routePlanId;

    private String vehicleNo;

    private LocalDate assignedDate;

    private String status;
    public  Long vehicleId;
}