package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.Collection.CollectionRequest;
import com.smartdms.operation_service.dto.Collection.CollectionResponse;

import java.util.List;

public interface CollectionService {

    CollectionResponse create(CollectionRequest request);

    CollectionResponse getById(Long id);

    List<CollectionResponse> getAll();

    List<CollectionResponse> getByInvoiceId(Long invoiceId);

    void delete(Long id);
}