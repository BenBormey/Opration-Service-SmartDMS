package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.WarehouseStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Long> {
}