package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderItemRepository  extends JpaRepository<SalesOrderItem, Long> {

    List<SalesOrderItem> findBySalesOrderId(Long salesOrderId);

    void deleteBySalesOrderId(Long salesOrderId);
}
