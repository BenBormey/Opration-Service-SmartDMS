package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.stock.StockLedgerRequest;
import com.smartdms.operation_service.dto.stock.StockLedgerResponse;
import com.smartdms.operation_service.entity.StockLedger;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.StockLedgerRepository;
import com.smartdms.operation_service.service.StockLedgerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockLedgerServiceImpl implements StockLedgerService {

    private final StockLedgerRepository repository;

    public StockLedgerServiceImpl(StockLedgerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<StockLedgerResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public StockLedgerResponse getById(Long id) {
        StockLedger ledger = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock ledger not found with Id : " + id));
        return toResponse(ledger);
    }

    @Override
    @Transactional
    public StockLedgerResponse create(StockLedgerRequest request) {
        StockLedger ledger = StockLedger.builder()
                .sdId(request.getSdId())
                .productId(request.getProductId())
                .trxType(request.getTrxType())
                .qtyIn(request.getQtyIn())
                .qtyOut(request.getQtyOut())
                .balanceQty(request.getBalanceQty())
                .trxDate(LocalDateTime.now())     // set automatically
                .build();
        return toResponse(repository.save(ledger));
    }

    @Override
    @Transactional
    public StockLedgerResponse update(Long id, StockLedgerRequest request) {
        StockLedger ledger = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock ledger not found with Id : " + id));
        ledger.setSdId(request.getSdId());
        ledger.setProductId(request.getProductId());
        ledger.setTrxType(request.getTrxType());
        ledger.setQtyIn(request.getQtyIn());
        ledger.setQtyOut(request.getQtyOut());
        ledger.setBalanceQty(request.getBalanceQty());
        return toResponse(repository.save(ledger));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        StockLedger ledger = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock ledger not found with Id : " + id));
        repository.delete(ledger);
    }

    private StockLedgerResponse toResponse(StockLedger l) {
        return StockLedgerResponse.builder()
                .id(l.getId())
                .sdId(l.getSdId())
                .productId(l.getProductId())
                .trxType(l.getTrxType())
                .qtyIn(l.getQtyIn())
                .qtyOut(l.getQtyOut())
                .balanceQty(l.getBalanceQty())
                .trxDate(l.getTrxDate())
                .build();
    }
}