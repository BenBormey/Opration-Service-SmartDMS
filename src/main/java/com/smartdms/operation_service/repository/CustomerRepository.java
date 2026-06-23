package com.smartdms.operation_service.repository;

import com.smartdms.operation_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
