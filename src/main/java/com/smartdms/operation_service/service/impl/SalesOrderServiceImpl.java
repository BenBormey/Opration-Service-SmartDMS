package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.salesorder.CreateSalesOrderItemRequest;
import com.smartdms.operation_service.dto.salesorder.SalesOrderItemResponse;
import com.smartdms.operation_service.dto.salesorder.SalesOrderRequest;
import com.smartdms.operation_service.dto.salesorder.SalesOrderResponse;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.entity.Product;
import com.smartdms.operation_service.entity.SalesOrder;
import com.smartdms.operation_service.entity.SalesOrderItem;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.repository.ProductRepository;
import com.smartdms.operation_service.repository.SalesOrderRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public SalesOrderResponse create(SalesOrderRequest request) {

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomerId(request.getCustomerId());
        salesOrder.setSalesmanId(request.getSalesmanId());
        salesOrder.setSdId(request.getSdId());
        salesOrder.setOrderDate(request.getOrderDate());
        salesOrder.setStatus("Pending");
        salesOrder.setOrderNo("SO-" + System.currentTimeMillis());
        salesOrder.setNote(request.getNote());
        salesOrder.setCreatedAt(LocalDateTime.now());
        salesOrder.setUpdatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        if (request.getItems() != null) {
            for (CreateSalesOrderItemRequest itemReq : request.getItems()) {
                BigDecimal lineTotal = itemReq.getUnitPrice().multiply(itemReq.getQty());

                SalesOrderItem item = SalesOrderItem.builder()
                        .productId(itemReq.getProductId())
                        .qty(itemReq.getQty())
                        .unitPrice(itemReq.getUnitPrice())
                        .lineTotal(lineTotal)
                        .build();

                salesOrder.addItem(item);   // sets both sides
                total = total.add(lineTotal);
            }
        }

        salesOrder.setTotalAmount(total);

        // cascade saves the items along with the order
        SalesOrder saved = salesOrderRepository.save(salesOrder);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponse getById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));
        return mapToResponse(salesOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponse> getAll() {
        return salesOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SalesOrderResponse update(Long id, SalesOrderRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));

        salesOrder.setCustomerId(request.getCustomerId());
        salesOrder.setSalesmanId(request.getSalesmanId());
        salesOrder.setSdId(request.getSdId());
        salesOrder.setOrderDate(request.getOrderDate());
        salesOrder.setNote(request.getNote());
        salesOrder.setUpdatedAt(LocalDateTime.now());

        SalesOrder updated = salesOrderRepository.save(salesOrder);
        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));
        salesOrderRepository.delete(salesOrder);
    }

    // ---------- mappers ----------

    private SalesOrderResponse mapToResponse(SalesOrder salesOrder) {

        String customerName = null;
        if (salesOrder.getCustomerId() != null) {
            Customer customer = customerRepository.findById(salesOrder.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            customerName = customer.getCustomerName();
        }

        String salesmanName = null;
        if (salesOrder.getSalesmanId() != null) {
            User salesman = userRepository.findById(salesOrder.getSalesmanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Salesman not found"));
            salesmanName = salesman.getFullName();
        }

        String sdName = null;
        if (salesOrder.getSdId() != null) {
            User sd = userRepository.findById(salesOrder.getSdId())
                    .orElseThrow(() -> new ResourceNotFoundException("SD not found"));
            sdName = sd.getFullName();
        }

        List<SalesOrderItemResponse> items = salesOrder.getItems()
                .stream()
                .map(this::mapItemToResponse)
                .toList();

        return SalesOrderResponse.builder()
                .id(salesOrder.getId())
                .orderNo(salesOrder.getOrderNo())
                .customerId(salesOrder.getCustomerId())
                .customerName(customerName)
                .salesmanId(salesOrder.getSalesmanId())
                .salesmanName(salesmanName)
                .sdId(salesOrder.getSdId())
                .sdName(sdName)
                .orderDate(salesOrder.getOrderDate())
                .status(salesOrder.getStatus())
                .totalAmount(salesOrder.getTotalAmount())
                .note(salesOrder.getNote())
                .createdAt(salesOrder.getCreatedAt())
                .updatedAt(salesOrder.getUpdatedAt())
                .items(items)
                .build();
    }

    private SalesOrderItemResponse mapItemToResponse(SalesOrderItem item) {

        String productName = null;
        if (item.getProductId() != null) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            productName = product.getProductName();
        }

        return SalesOrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(productName)
                .qty(item.getQty())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }
}