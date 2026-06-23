package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    
}
