package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.StockLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockLedgerRepository extends JpaRepository<StockLedger, Long> {
    Optional<StockLedger> findFirstByProductIdAndWarehouseIdOrderByIdDesc(Long productId, Long warehouseId);

    // latest balance for a product at an SD (destination side)
    Optional<StockLedger> findFirstByProductIdAndSdIdOrderByIdDesc(Long productId, Long sdId);

}
