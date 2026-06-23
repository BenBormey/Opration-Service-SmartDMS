package com.smartdms.operation_service.repository;



import com.smartdms.operation_service.entity.Sd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SdRepository extends JpaRepository<Sd, Long> {

    boolean existsBySdCode(String sdCode);
}