package com.smartdms.operation_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "transfer_orders", schema = "dms")
public class TransferOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_no")
    private String transferNo;

    @Column(name = "from_warehouse_id")
    private Long fromWarehouseId;

    @Column(name = "to_sd_id")
    private Long toSdId;

    @Column(name = "transfer_date")
    private LocalDateTime transferDate;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt =LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt= LocalDateTime.now();




    @OneToMany(
            mappedBy = "transferOrder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TransferOrderItem> transferOrderItems = new ArrayList<>();
}