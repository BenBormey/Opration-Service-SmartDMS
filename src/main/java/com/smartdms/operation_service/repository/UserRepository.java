package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}