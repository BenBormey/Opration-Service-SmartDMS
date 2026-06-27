package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.RoutePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutePlanRepository extends JpaRepository<RoutePlan, Long> {

    List<RoutePlan> findBySalesmanId(Long salesmanId);

    List<RoutePlan> findByDayOfWeek(String dayOfWeek);

    List<RoutePlan> findBySalesmanIdAndDayOfWeek(Long salesmanId, String dayOfWeek);
}