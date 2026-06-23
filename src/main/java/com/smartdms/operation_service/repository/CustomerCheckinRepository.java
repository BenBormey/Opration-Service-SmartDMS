package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.CustomerCheckin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCheckinRepository extends JpaRepository<CustomerCheckin , Long> {
}
