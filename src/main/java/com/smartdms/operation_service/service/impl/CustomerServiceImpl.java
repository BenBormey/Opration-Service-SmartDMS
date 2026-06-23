package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Customer.CustomerRequest;
import com.smartdms.operation_service.dto.Customer.CustomerResponse;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {

        Customer customer = new Customer();

        customer.setCustomerCode(request.getCustomerCode());
        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setSalesmanId(request.getSalesmanId());
        customer.setSdId(request.getSdId());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setIsActive(request.getIsActive());
        customer.setCreatedAt(LocalDateTime.now());

        Customer saved = repository.save(customer);

        return mapToResponse(saved);
    }

    @Override
    public CustomerResponse getById(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return mapToResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest request) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setCustomerCode(request.getCustomerCode());
        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setSalesmanId(request.getSalesmanId());
        customer.setSdId(request.getSdId());
        customer.setCreditLimit(request.getCreditLimit());
        customer.setIsActive(request.getIsActive());

        Customer updated = repository.save(customer);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        repository.delete(customer);
    }

    private CustomerResponse mapToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setCustomerCode(customer.getCustomerCode());
        response.setCustomerName(customer.getCustomerName());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setSalesmanId(customer.getSalesmanId());
        response.setSdId(customer.getSdId());
        response.setCreditLimit(customer.getCreditLimit());
        response.setIsActive(customer.getIsActive());
        response.setCreatedAt(customer.getCreatedAt());

        return response;
    }
}