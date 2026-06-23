package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouse_stocks", schema = "dms")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class WarehouseStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // warehouse_id → warehouses table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    // product_id → products table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "qty_on_hand")
    private Integer qtyOnHand;
}