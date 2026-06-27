package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Long> {
    Optional<WarehouseStock> findByWarehouse_IdAndProduct_Id(
            Long warehouseId,
            Long productId
    );
}