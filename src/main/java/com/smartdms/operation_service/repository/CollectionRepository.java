package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByInvoiceId(Long invoiceId);

    List<Collection> findByCustomerId(Long customerId);

    List<Collection> findByCollectedBy(Long collectedBy);
}