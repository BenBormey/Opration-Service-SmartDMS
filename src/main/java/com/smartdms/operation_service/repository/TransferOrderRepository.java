package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.TransferOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransferOrderRepository extends JpaRepository<TransferOrder, Long> {
    List<TransferOrder> findByDriverIdAndTransferDateBetweenOrderByIdAsc(
            Long driverId, LocalDateTime start, LocalDateTime end);
}
