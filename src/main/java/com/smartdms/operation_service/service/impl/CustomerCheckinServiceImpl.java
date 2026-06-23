package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.CustomerCheckin.CustomerCheckinRequest;
import com.smartdms.operation_service.dto.CustomerCheckin.CustomerCheckinResponse;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.entity.CustomerCheckin;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CustomerCheckinRepository;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.CustomerCheckinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerCheckinServiceImpl implements CustomerCheckinService {

    private final CustomerCheckinRepository repository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public CustomerCheckinServiceImpl(CustomerCheckinRepository repository,
                                      UserRepository userRepository,
                                      CustomerRepository customerRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerCheckinResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CustomerCheckinResponse getById(Long id) {
        CustomerCheckin checkin = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checkin not found with Id : " + id));
        return toResponse(checkin);
    }

    @Override
    @Transactional
    public CustomerCheckinResponse create(CustomerCheckinRequest request) {
        User salesman = userRepository.findById(request.getSalesmanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salesman not found with Id : " + request.getSalesmanId()));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with Id : " + request.getCustomerId()));

        CustomerCheckin checkin = CustomerCheckin.builder()
                .salesman(salesman)
                .customer(customer)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .checkinTime(LocalDateTime.now())
                .build();

        return toResponse(repository.save(checkin));
    }

    @Override
    @Transactional
    public CustomerCheckinResponse update(Long id, CustomerCheckinRequest request) {
        CustomerCheckin checkin = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checkin not found with Id : " + id));

        User salesman = userRepository.findById(request.getSalesmanId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Salesman not found with Id : " + request.getSalesmanId()));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with Id : " + request.getCustomerId()));

        checkin.setSalesman(salesman);
        checkin.setCustomer(customer);
        checkin.setLatitude(request.getLatitude());
        checkin.setLongitude(request.getLongitude());

        return toResponse(repository.save(checkin));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CustomerCheckin checkin = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Checkin not found with Id : " + id));
        repository.delete(checkin);
    }

    // ---- helper: entity -> response (names come from joined tables) ----
    private CustomerCheckinResponse toResponse(CustomerCheckin c) {
        return CustomerCheckinResponse.builder()
                .id(c.getId())
                .salesmanId(c.getSalesman().getId())
                .salesmanName(c.getSalesman().getFullName())        // users.full_name
                .customerId(c.getCustomer().getId())
                .customerName(c.getCustomer().getCustomerName())    // customers.customer_name
                .checkinTime(c.getCheckinTime())
                .latitude(c.getLatitude())
                .longitude(c.getLongitude())
                .build();
    }
}