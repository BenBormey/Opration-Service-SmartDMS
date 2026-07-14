package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
