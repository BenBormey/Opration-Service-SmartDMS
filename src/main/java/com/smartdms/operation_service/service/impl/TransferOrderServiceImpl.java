package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.entity.*;
import com.smartdms.operation_service.exception.InvalidStateException;
import com.smartdms.operation_service.repository.StockLedgerRepository;
import com.smartdms.operation_service.repository.StockRepository;
import com.smartdms.operation_service.repository.TransferOrderRepository;
import com.smartdms.operation_service.repository.WarehouseStockRepository;
import com.smartdms.operation_service.service.ITransferOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferOrderServiceImpl implements ITransferOrderService {

    // status constants — avoids typos from raw strings
    private static final String REQUEST   = "REQUEST";
    private static final String APPROVED  = "APPROVED";
    private static final String SHIPPED   = "SHIPPED";
    private static final String RECEIVED  = "RECEIVED";
    private static final String CANCELLED = "CANCELLED";
    private static final String TRX_IN  = "IN";
    private static final String TRX_OUT = "OUT";

    private final TransferOrderRepository transferOrderRepository;
    private final StockLedgerRepository stockLedgerRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional
    public TransferOrder save(TransferOrder transferOrder) {
        LocalDateTime now = LocalDateTime.now();
        transferOrder.setCreatedAt(now);
        transferOrder.setUpdatedAt(now);

        if (transferOrder.getStatus() == null) {
            transferOrder.setStatus(REQUEST);
        }

        if (transferOrder.getTransferOrderItems() != null) {
            for (TransferOrderItem item : transferOrder.getTransferOrderItems()) {
                item.setTransferOrder(transferOrder); // wire up the back-reference
            }
        }
        return transferOrderRepository.save(transferOrder);
    }

    @Override
    @Transactional
    public TransferOrder update(Long id, TransferOrder changes) {
        TransferOrder existing = findById(id);

        // copy only the editable header fields; preserve createdAt, status, items
        existing.setTransferNo(changes.getTransferNo());
        existing.setFromWarehouseId(changes.getFromWarehouseId());
        existing.setToSdId(changes.getToSdId());
        existing.setTransferDate(changes.getTransferDate());

        // 👈 driver fields (only overwrite when provided, so update doesn't wipe them)
        if (changes.getDriverId() != null)  existing.setDriverId(changes.getDriverId());
        if (changes.getVehicleId() != null) existing.setVehicleId(changes.getVehicleId());
        if (changes.getRemark() != null)    existing.setRemark(changes.getRemark());

        existing.setUpdatedAt(LocalDateTime.now());

        return transferOrderRepository.save(existing);
    }

    @Override
    public List<TransferOrder> findAll() {
        return transferOrderRepository.findAll();
    }

    @Override
    public TransferOrder findById(Long id) {
        return transferOrderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transfer Order not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        transferOrderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TransferOrder approve(Long id) {
        TransferOrder order = findById(id);
        requireStatus(order, REQUEST);
        return changeStatus(order, APPROVED);
    }

    @Override
    @Transactional
    public TransferOrder ship(Long id) {
        TransferOrder order = findById(id);
        requireStatus(order, APPROVED);           // must be approved before shipping
        // stock leaves the source warehouse
        for (TransferOrderItem item : order.getTransferOrderItems()) {
            recordOutbound(order, item);
        }
        order.setStartedAt(LocalDateTime.now());  // 👈 driver started moving stock
        return changeStatus(order, SHIPPED);
    }

    @Override
    @Transactional
    public TransferOrder receive(Long id) {
        TransferOrder order = findById(id);
        requireStatus(order, SHIPPED);            // must be shipped before receiving
        // stock arrives at the destination SD
        for (TransferOrderItem item : order.getTransferOrderItems()) {
            recordInbound(order, item);
        }
        order.setCompletedAt(LocalDateTime.now()); // 👈 driver finished
        return changeStatus(order, RECEIVED);
    }

    @Override
    @Transactional
    public TransferOrder cancel(Long id) {
        TransferOrder order = findById(id);
        // only cancel before goods have moved
        if (!REQUEST.equals(order.getStatus()) && !APPROVED.equals(order.getStatus())) {
            throw new RuntimeException(
                    "Cannot cancel an order in status " + order.getStatus());
        }
        return changeStatus(order, CANCELLED);
    }

    // ---- helpers ----

    private TransferOrder changeStatus(TransferOrder order, String status) {
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return transferOrderRepository.save(order);
    }

    private void requireStatus(TransferOrder order, String expected) {
        if (!expected.equals(order.getStatus())) {
            throw new InvalidStateException(
                    "Expected status " + expected + " but was " + order.getStatus());
        }
    }

    private void recordOutbound(TransferOrder order, TransferOrderItem item) {
        int qty = toQty(item.getQty());

        WarehouseStock stock = warehouseStockRepository
                .findByWarehouse_IdAndProduct_Id(order.getFromWarehouseId(), item.getProductId())
                .orElseThrow(() -> new InvalidStateException(
                        "No stock for product " + item.getProductId()
                                + " in warehouse " + order.getFromWarehouseId()));

        int onHand = stock.getQtyOnHand() == null ? 0 : stock.getQtyOnHand();
        if (onHand < qty) {
            throw new InvalidStateException(
                    "Not enough stock for product " + item.getProductId()
                            + ". Have " + onHand + ", need " + qty);
        }

        stock.setQtyOnHand(onHand - qty);
        stock.setUpdatedAt(LocalDateTime.now());
        warehouseStockRepository.save(stock);

        int prevBalance = stockLedgerRepository
                .findFirstByProductIdAndWarehouseIdOrderByIdDesc(
                        item.getProductId(), order.getFromWarehouseId())
                .map(StockLedger::getBalanceQty)
                .orElse(0);

        stockLedgerRepository.save(StockLedger.builder()
                .productId(item.getProductId())
                .warehouseId(order.getFromWarehouseId())
                .trxType(TRX_OUT)
                .qtyIn(0)
                .qtyOut(qty)
                .balanceQty(prevBalance - qty)
                .trxDate(LocalDateTime.now())
                .build());
    }

    private void recordInbound(TransferOrder order, TransferOrderItem item) {
        int qty = toQty(item.getQty());

        // 1. add stock into SD (stocks)
        Stock stock = stockRepository
                .findBySdIdAndProductId(order.getToSdId(), item.getProductId())
                .orElseGet(() -> Stock.builder()
                        .sdId(order.getToSdId())
                        .productId(item.getProductId())
                        .qtyOnHand(0)
                        .build());

        int onHand = stock.getQtyOnHand() == null ? 0 : stock.getQtyOnHand();
        stock.setQtyOnHand(onHand + qty);
        stockRepository.save(stock);

        // 2. ledger row (IN) — sd only, no warehouseId
        int prevBalance = stockLedgerRepository
                .findFirstByProductIdAndSdIdOrderByIdDesc(
                        item.getProductId(), order.getToSdId())
                .map(StockLedger::getBalanceQty)
                .orElse(0);

        stockLedgerRepository.save(StockLedger.builder()
                .productId(item.getProductId())
                .sdId(order.getToSdId())
                .trxType(TRX_IN)
                .qtyIn(qty)
                .qtyOut(0)
                .balanceQty(prevBalance + qty)
                .trxDate(LocalDateTime.now())
                .build());
    }

    private int toQty(Double qty) {
        return qty == null ? 0 : qty.intValue();
    }
}