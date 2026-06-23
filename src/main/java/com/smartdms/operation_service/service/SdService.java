package com.smartdms.operation_service.service;

import com.smartdms.operation_service.dto.sd.SdRequest;
import com.smartdms.operation_service.dto.sd.SdResponse;

import java.util.List;

public interface SdService {

    SdResponse create(SdRequest request);

    SdResponse getById(Long id);

    List<SdResponse> getAll();

    SdResponse update(Long id, SdRequest request);

    void delete(Long id);
}