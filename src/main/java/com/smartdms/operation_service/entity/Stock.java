package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stocks", schema = "dms")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sd_id")
    private Long sdId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "qty_on_hand")
    private Integer qtyOnHand;
}