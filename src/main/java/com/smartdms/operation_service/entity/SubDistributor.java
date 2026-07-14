package com.smartdms.operation_service.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sub_distributors", schema = "dms")
@Data
public class SubDistributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sd_code")
    private String sdCode;

    @Column(name = "sd_name")
    private String sdName;

    private String phone;

    private String address;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}