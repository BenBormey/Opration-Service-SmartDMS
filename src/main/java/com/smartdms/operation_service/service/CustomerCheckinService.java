package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.customercheckin.CustomerCheckinRequest;
import com.smartdms.operation_service.dto.customercheckin.CustomerCheckinResponse;

import java.util.List;

public interface CustomerCheckinService {

    List<CustomerCheckinResponse> getAll();

    CustomerCheckinResponse getById(Long id);

    CustomerCheckinResponse create(CustomerCheckinRequest request);

    CustomerCheckinResponse update(Long id, CustomerCheckinRequest request);

    void delete(Long id);
}