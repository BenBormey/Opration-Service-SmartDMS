package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Invoice.InvoiceRequest;
import com.smartdms.operation_service.dto.Invoice.InvoiceResponse;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.entity.Invoice;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.repository.InvoiceRepository;
import com.smartdms.operation_service.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    @Override
    public InvoiceResponse create(InvoiceRequest request) {

        if (request.getInvoiceNo() != null
                && invoiceRepository.existsByInvoiceNo(request.getInvoiceNo())) {
            throw new ResourceAlreadyExistsException(
                    "Invoice number already exists: " + request.getInvoiceNo());
        }

        // one invoice per sales order — block duplicates
        if (request.getSalesOrderId() != null
                && invoiceRepository.existsBySalesOrderId(request.getSalesOrderId())) {
            throw new ResourceAlreadyExistsException(
                    "Invoice already exists for sales order id: " + request.getSalesOrderId());
        }

        BigDecimal total = request.getTotalAmount() != null ? request.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal paid = request.getPaidAmount() != null ? request.getPaidAmount() : BigDecimal.ZERO;

        Invoice invoice = new Invoice();
        invoice.setInvoiceNo(request.getInvoiceNo());
        invoice.setSalesOrderId(request.getSalesOrderId());
        invoice.setCustomerId(request.getCustomerId());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setTotalAmount(total);
        invoice.setPaidAmount(paid);
        invoice.setStatus(resolveStatus(total, paid, request.getStatus()));
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice saved = invoiceRepository.save(invoice);
        return mapToResponse(saved);
    }

    @Override
    public InvoiceResponse update(Long id, InvoiceRequest request) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        invoice.setSalesOrderId(request.getSalesOrderId());
        invoice.setCustomerId(request.getCustomerId());
        if (request.getTotalAmount() != null) {
            invoice.setTotalAmount(request.getTotalAmount());
        }
        if (request.getPaidAmount() != null) {
            invoice.setPaidAmount(request.getPaidAmount());
        }
        invoice.setStatus(resolveStatus(invoice.getTotalAmount(), invoice.getPaidAmount(), request.getStatus()));
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice updated = invoiceRepository.save(invoice);
        return mapToResponse(updated);
    }

    @Override
    public InvoiceResponse getById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        return mapToResponse(invoice);
    }

    @Override
    public List<InvoiceResponse> getAll() {

        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        // No is_deleted column on this table, so this is a hard delete.
        invoiceRepository.delete(invoice);
    }

    // ---------- helpers ----------

    // Derive status from amounts unless the caller forced one explicitly.
    private String resolveStatus(BigDecimal total, BigDecimal paid, String requestedStatus) {
        if (requestedStatus != null && !requestedStatus.isBlank()) {
            return requestedStatus;
        }
        BigDecimal t = total != null ? total : BigDecimal.ZERO;
        BigDecimal p = paid != null ? paid : BigDecimal.ZERO;

        if (p.compareTo(BigDecimal.ZERO) <= 0) {
            return "UNPAID";
        }
        if (p.compareTo(t) >= 0) {
            return "PAID";
        }
        return "PARTIAL";
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {

        String customerName = null;
        if (invoice.getCustomerId() != null) {
            customerName = customerRepository.findById(invoice.getCustomerId())
                    .map(Customer::getCustomerName)
                    .orElse(null);
        }

        BigDecimal total = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal paid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNo(invoice.getInvoiceNo())
                .salesOrderId(invoice.getSalesOrderId())
                .customerId(invoice.getCustomerId())
                .customerName(customerName)
                .invoiceDate(invoice.getInvoiceDate())
                .totalAmount(invoice.getTotalAmount())
                .paidAmount(invoice.getPaidAmount())
                .balanceDue(total.subtract(paid))
                .status(invoice.getStatus())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}