package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_categories", schema = "dms")
@Data
public class categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;
}