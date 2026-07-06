package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCustomerId(Long customerId);

    boolean existsByInvoiceNo(String invoiceNo);

    boolean existsBySalesOrderId(Long salesOrderId);
}