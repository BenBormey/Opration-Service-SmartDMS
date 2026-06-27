package com.smartdms.operation_service.service;

import com.smartdms.operation_service.entity.TransferOrder;

import java.util.List;

public interface ITransferOrderService {

    TransferOrder save(TransferOrder transferOrder);

    TransferOrder update(Long id, TransferOrder changes);   // <-- added

    List<TransferOrder> findAll();

    TransferOrder findById(Long id);

    void delete(Long id);

    TransferOrder approve(Long id);

    TransferOrder ship(Long id);

    TransferOrder receive(Long id);

    TransferOrder cancel(Long id);
}