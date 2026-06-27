package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.TransferOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferOrderRepository extends JpaRepository<TransferOrder, Long> {
}
