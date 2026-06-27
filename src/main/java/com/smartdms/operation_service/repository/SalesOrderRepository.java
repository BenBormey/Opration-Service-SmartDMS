package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder,Long> {
    Optional<SalesOrder> findByOrderNo(String orderNo);

    List<SalesOrder> findByCustomerId(Long customerId);

    List<SalesOrder> findBySalesmanId(Long salesmanId);

    List<SalesOrder> findByStatus(String status);

    boolean existsByOrderNo(String orderNo);
}
