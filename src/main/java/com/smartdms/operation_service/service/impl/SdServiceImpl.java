package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.sd.SdRequest;
import com.smartdms.operation_service.dto.sd.SdResponse;
import com.smartdms.operation_service.entity.sub_distributors;
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

        sub_distributors subdistributors = new sub_distributors();
        subdistributors.setSdCode(request.getSdCode());
        subdistributors.setSdName(request.getSdName());
        subdistributors.setPhone(request.getPhone());
        subdistributors.setAddress(request.getAddress());
        subdistributors.setIsActive(request.getIsActive());
        subdistributors.setCreatedAt(LocalDateTime.now());

        sub_distributors saved  = repository.save(subdistributors);
        return mapToResponse(saved);
    }

    @Override
    public SdResponse getById(Long id) {
        sub_distributors subdistributors = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));
        return mapToResponse(subdistributors);

    }

    @Override
    public List<SdResponse> getAll() {
        List<sub_distributors> subdistributors = repository.findAll();

        return subdistributors.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SdResponse update(Long id, SdRequest request) {
        sub_distributors subdistributors = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));

        subdistributors.setSdCode(request.getSdCode());
        subdistributors.setSdName(request.getSdName());
        subdistributors.setPhone(request.getPhone());
        subdistributors.setAddress(request.getAddress());
        subdistributors.setIsActive(request.getIsActive());

        sub_distributors updated = repository.save(subdistributors);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        sub_distributors subdistributors = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SD not found"));

        repository.delete(subdistributors);
    }
    private SdResponse mapToResponse(sub_distributors subdistributors) {

        SdResponse response = new SdResponse();

        response.setId(subdistributors.getId());
        response.setSdCode(subdistributors.getSdCode());
        response.setSdName(subdistributors.getSdName());
        response.setPhone(subdistributors.getPhone());
        response.setAddress(subdistributors.getAddress());
        response.setIsActive(subdistributors.getIsActive());
        response.setCreatedAt(subdistributors.getCreatedAt());

        return response;
    }
}
