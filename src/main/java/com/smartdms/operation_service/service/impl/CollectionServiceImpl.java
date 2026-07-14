package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.collection.CollectionRequest;
import com.smartdms.operation_service.dto.collection.CollectionResponse;
import com.smartdms.operation_service.entity.Collection;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.entity.Invoice;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CollectionRepository;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.repository.InvoiceRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CollectionResponse create(CollectionRequest request) {

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Collection amount must be greater than zero");
        }

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Invoice not found with id: " + request.getInvoiceId()));

        BigDecimal total = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal alreadyPaid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal balance = total.subtract(alreadyPaid);

        if (request.getAmount().compareTo(balance) > 0) {
            throw new IllegalArgumentException(
                    "Collection (" + request.getAmount() + ") exceeds remaining balance (" + balance + ")");
        }

        // 1. save the collection
        Collection collection = new Collection();
        collection.setInvoiceId(invoice.getId());
        // fall back to the invoice's customer if the request didn't send one
        collection.setCustomerId(request.getCustomerId() != null
                ? request.getCustomerId() : invoice.getCustomerId());
        collection.setAmount(request.getAmount());
        collection.setPaymentMethod(request.getPaymentMethod());
        collection.setCollectedBy(request.getCollectedBy());
        collection.setCollectionDate(LocalDateTime.now());
        Collection saved = collectionRepository.save(collection);

        // 2. update the invoice paid amount + status
        BigDecimal newPaid = alreadyPaid.add(request.getAmount());
        invoice.setPaidAmount(newPaid);
        invoice.setStatus(resolveStatus(total, newPaid));
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        return mapToResponse(saved, invoice);
    }

    @Override
    public CollectionResponse getById(Long id) {

        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + id));

        Invoice invoice = invoiceRepository.findById(collection.getInvoiceId()).orElse(null);
        return mapToResponse(collection, invoice);
    }

    @Override
    public List<CollectionResponse> getAll() {

        return collectionRepository.findAll()
                .stream()
                .map(c -> mapToResponse(c, invoiceRepository.findById(c.getInvoiceId()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CollectionResponse> getByInvoiceId(Long invoiceId) {

        return collectionRepository.findByInvoiceId(invoiceId)
                .stream()
                .map(c -> mapToResponse(c, invoiceRepository.findById(c.getInvoiceId()).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + id));

        // reverse this collection off the invoice before deleting
        Invoice invoice = invoiceRepository.findById(collection.getInvoiceId()).orElse(null);
        if (invoice != null) {
            BigDecimal total = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
            BigDecimal paid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
            BigDecimal newPaid = paid.subtract(collection.getAmount());
            if (newPaid.compareTo(BigDecimal.ZERO) < 0) {
                newPaid = BigDecimal.ZERO;
            }
            invoice.setPaidAmount(newPaid);
            invoice.setStatus(resolveStatus(total, newPaid));
            invoice.setUpdatedAt(LocalDateTime.now());
            invoiceRepository.save(invoice);
        }

        collectionRepository.delete(collection);
    }

    // ---------- helpers ----------

    private String resolveStatus(BigDecimal total, BigDecimal paid) {
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

    private CollectionResponse mapToResponse(Collection collection, Invoice invoice) {

        String customerName = null;
        if (collection.getCustomerId() != null) {
            customerName = customerRepository.findById(collection.getCustomerId())
                    .map(Customer::getCustomerName)
                    .orElse(null);
        }

        String collectedByName = null;
        if (collection.getCollectedBy() != null) {
            collectedByName = userRepository.findById(collection.getCollectedBy())
                    .map(User::getFullName)
                    .orElse(null);
        }

        CollectionResponse.CollectionResponseBuilder builder = CollectionResponse.builder()
                .id(collection.getId())
                .invoiceId(collection.getInvoiceId())
                .customerId(collection.getCustomerId())
                .customerName(customerName)
                .amount(collection.getAmount())
                .paymentMethod(collection.getPaymentMethod())
                .collectionDate(collection.getCollectionDate())
                .collectedBy(collection.getCollectedBy())
                .collectedByName(collectedByName);

        if (invoice != null) {
            BigDecimal total = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO;
            BigDecimal paid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
            builder.invoiceNo(invoice.getInvoiceNo())
                    .invoiceTotal(total)
                    .invoicePaid(paid)
                    .invoiceBalance(total.subtract(paid))
                    .invoiceStatus(invoice.getStatus());
        }

        return builder.build();
    }
}