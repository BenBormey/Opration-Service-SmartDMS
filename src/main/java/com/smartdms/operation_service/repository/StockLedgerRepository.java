package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.StockLedger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockLedgerRepository extends JpaRepository<StockLedger, Long> {
}
