package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "dms")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "password_hash")
    private String passwordHash;   // ⚠ never put this in a Response DTO

    @Column(name = "full_name")
    private String fullName;       // ✅ this is the salesman name

    private String phone;

    private String email;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "sd_id")
    private Long sdId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}