package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByBarcode(String barcode);

    List<Product> findByIsDeletedFalse();
}
