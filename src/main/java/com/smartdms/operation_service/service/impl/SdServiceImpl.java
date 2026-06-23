package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.sd.SdRequest;
import com.smartdms.operation_service.dto.sd.SdResponse;
import com.smartdms.operation_service.entity.Sd;
import com.smartdms.operation_service.exception.ResourceAlreadyExistsException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.SdRepository;
import com.smartdms.operation_service.service.SdService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class SdServiceImpl implements SdService {


    private  final SdRepository repository;

    public SdServiceImpl(SdRepository repository) {
        this.repository = repository;
    }

    @Override
    public SdResponse create(SdRequest request) {
        if (repository.existsBySdCode(request.getSdCode())){
            throw  new ResourceAlreadyExistsException("SD code already exists");
        }

        Sd sd = new Sd();
        sd.setSdCode(request.getSdCode());
        sd.setSdName(request.getSdName());
        sd.setPhone(request.getPhone());
        sd.setAddress(request.getAddress());
        sd.setIsActive(request.getIsActive());
        sd.setCreatedAt(LocalDateTime.now());

        Sd saved  = repository.save(sd);
        return mapToResponse(saved);
    }

    @Override
    public SdResponse getById(Long id) {
        Sd sd = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));
        return mapToResponse(sd);

    }

    @Override
    public List<SdResponse> getAll() {
        List<Sd> sds = repository.findAll();

        return sds.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SdResponse update(Long id, SdRequest request) {
        Sd sd = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));

        sd.setSdCode(request.getSdCode());
        sd.setSdName(request.getSdName());
        sd.setPhone(request.getPhone());
        sd.setAddress(request.getAddress());
        sd.setIsActive(request.getIsActive());

        Sd updated = repository.save(sd);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Sd sd = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));

        repository.delete(sd);
    }
    private SdResponse mapToResponse(Sd sd) {

        SdResponse response = new SdResponse();

        response.setId(sd.getId());
        response.setSdCode(sd.getSdCode());
        response.setSdName(sd.getSdName());
        response.setPhone(sd.getPhone());
        response.setAddress(sd.getAddress());
        response.setIsActive(sd.getIsActive());
        response.setCreatedAt(sd.getCreatedAt());

        return response;
    }
}
