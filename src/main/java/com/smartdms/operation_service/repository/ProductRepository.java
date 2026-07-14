package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByBarcode(String barcode);

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByIsDeletedFalse();
}
