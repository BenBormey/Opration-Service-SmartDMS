package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySdIdAndProductId(Long sdId, Long productId);
}
