package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.CustomerCheckin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerCheckinRepository extends JpaRepository<CustomerCheckin , Long> {

    List<CustomerCheckin> findBySalesman_IdAndCheckinTimeBetween(
            Long salesmanId, LocalDateTime start, LocalDateTime end);
}
