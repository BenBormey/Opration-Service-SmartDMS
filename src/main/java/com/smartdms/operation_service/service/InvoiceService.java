package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.Invoice.InvoiceRequest;
import com.smartdms.operation_service.dto.Invoice.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse create(InvoiceRequest request);

    InvoiceResponse update(Long id, InvoiceRequest request);

    InvoiceResponse getById(Long id);

    List<InvoiceResponse> getAll();

    void delete(Long id);
}