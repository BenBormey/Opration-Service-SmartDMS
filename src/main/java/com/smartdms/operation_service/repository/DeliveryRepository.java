package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Delivery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    boolean existsByDeliveryNo(String deliveryNo);

    List<Delivery> findByDriverIdAndDeliveryDateOrderByIdAsc(Long driverId, LocalDate date);

    Optional<Delivery> findFirstByDriverIdAndDeliveryDateAndStatusOrderByIdAsc(
            Long driverId, LocalDate date, String status);

    List<Delivery> findByDriverIdAndStatusOrderByCompletedAtDesc(
            Long driverId, String status, Pageable pageable);
}