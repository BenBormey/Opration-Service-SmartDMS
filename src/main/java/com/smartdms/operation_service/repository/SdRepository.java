package com.smartdms.operation_service.repository;



import com.smartdms.operation_service.entity.sub_distributors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdRepository extends JpaRepository<sub_distributors, Long> {

    boolean existsBySdCode(String sdCode);
}