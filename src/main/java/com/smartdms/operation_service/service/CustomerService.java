package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.customer.CustomerResponse;
import com.smartdms.operation_service.dto.customer.CustomerRequest;


import java.util.List;

public interface CustomerService {

    CustomerResponse create(CustomerRequest request);

    CustomerResponse getById(Long id);

    List<CustomerResponse> getAll();

    CustomerResponse update(Long id, CustomerRequest request);

    void delete(Long id);
}