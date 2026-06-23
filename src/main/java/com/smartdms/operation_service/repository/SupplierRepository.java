package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
    boolean existsBySupplierCode(String supplierCode);

    List<Supplier> findByIsDeletedFalse();
}
