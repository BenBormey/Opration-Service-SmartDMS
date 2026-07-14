package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.transferorder.TransferOrderItemRequest;
import com.smartdms.operation_service.dto.transferorder.TransferOrderItemResponse;
import com.smartdms.operation_service.dto.transferorder.TransferOrderRequest;
import com.smartdms.operation_service.dto.transferorder.TransferOrderResponse;
import com.smartdms.operation_service.entity.*;
import com.smartdms.operation_service.exception.InvalidStateException;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.ProductRepository;
import com.smartdms.operation_service.repository.StockLedgerRepository;
import com.smartdms.operation_service.repository.StockRepository;
import com.smartdms.operation_service.repository.TransferOrderRepository;
import com.smartdms.operation_service.repository.WarehouseStockRepository;
import com.smartdms.operation_service.service.TransferOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferOrderServiceImpl implements TransferOrderService {

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
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public TransferOrderResponse create(TransferOrderRequest request) {
        LocalDateTime now = LocalDateTime.now();

        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferNo(request.getTransferNo());
        transferOrder.setFromWarehouseId(request.getFromWarehouseId());
        transferOrder.setToSdId(request.getToSdId());
        transferOrder.setDriverId(request.getDriverId());
        transferOrder.setVehicleId(request.getVehicleId());
        transferOrder.setTransferDate(request.getTransferDate());
        transferOrder.setRemark(request.getRemark());
        transferOrder.setStatus(REQUEST);
        transferOrder.setCreatedAt(now);
        transferOrder.setUpdatedAt(now);

        if (request.getItems() != null) {
            for (TransferOrderItemRequest itemReq : request.getItems()) {
                TransferOrderItem item = new TransferOrderItem();
                item.setProductId(itemReq.getProductId());
                item.setQty(itemReq.getQty());
                item.setTransferOrder(transferOrder); // wire up the back-reference
                transferOrder.getTransferOrderItems().add(item);
            }
        }

        TransferOrder saved = transferOrderRepository.save(transferOrder);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public TransferOrderResponse update(Long id, TransferOrderRequest request) {
        TransferOrder existing = getEntityById(id);

        // copy only the editable header fields; preserve createdAt, status, items
        existing.setTransferNo(request.getTransferNo());
        existing.setFromWarehouseId(request.getFromWarehouseId());
        existing.setToSdId(request.getToSdId());
        existing.setTransferDate(request.getTransferDate());

        // driver fields (only overwrite when provided, so update doesn't wipe them)
        if (request.getDriverId() != null)  existing.setDriverId(request.getDriverId());
        if (request.getVehicleId() != null) existing.setVehicleId(request.getVehicleId());
        if (request.getRemark() != null)    existing.setRemark(request.getRemark());

        existing.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(transferOrderRepository.save(existing));
    }

    @Override
    public List<TransferOrderResponse> getAll() {
        return transferOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TransferOrderResponse getById(Long id) {
        return mapToResponse(getEntityById(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        transferOrderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TransferOrderResponse approve(Long id) {
        TransferOrder order = getEntityById(id);
        requireStatus(order, REQUEST);
        return mapToResponse(changeStatus(order, APPROVED));
    }

    @Override
    @Transactional
    public TransferOrderResponse ship(Long id) {
        TransferOrder order = getEntityById(id);
        requireStatus(order, APPROVED);           // must be approved before shipping
        // stock leaves the source warehouse
        for (TransferOrderItem item : order.getTransferOrderItems()) {
            recordOutbound(order, item);
        }
        order.setStartedAt(LocalDateTime.now());  // driver started moving stock
        return mapToResponse(changeStatus(order, SHIPPED));
    }

    @Override
    @Transactional
    public TransferOrderResponse receive(Long id) {
        TransferOrder order = getEntityById(id);
        requireStatus(order, SHIPPED);            // must be shipped before receiving
        // stock arrives at the destination SD
        for (TransferOrderItem item : order.getTransferOrderItems()) {
            recordInbound(order, item);
        }
        order.setCompletedAt(LocalDateTime.now()); // driver finished
        return mapToResponse(changeStatus(order, RECEIVED));
    }

    @Override
    @Transactional
    public TransferOrderResponse cancel(Long id) {
        TransferOrder order = getEntityById(id);
        // only cancel before goods have moved
        if (!REQUEST.equals(order.getStatus()) && !APPROVED.equals(order.getStatus())) {
            throw new InvalidStateException(
                    "Cannot cancel an order in status " + order.getStatus());
        }
        return mapToResponse(changeStatus(order, CANCELLED));
    }

    // ---- helpers ----

    private TransferOrder getEntityById(Long id) {
        return transferOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transfer Order not found with id: " + id));
    }

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

    private TransferOrderResponse mapToResponse(TransferOrder order) {
        List<TransferOrderItemResponse> items = order.getTransferOrderItems()
                .stream()
                .map(this::mapItemToResponse)
                .toList();

        return TransferOrderResponse.builder()
                .id(order.getId())
                .transferNo(order.getTransferNo())
                .fromWarehouseId(order.getFromWarehouseId())
                .toSdId(order.getToSdId())
                .driverId(order.getDriverId())
                .vehicleId(order.getVehicleId())
                .transferDate(order.getTransferDate())
                .status(order.getStatus())
                .startedAt(order.getStartedAt())
                .completedAt(order.getCompletedAt())
                .remark(order.getRemark())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }

    private TransferOrderItemResponse mapItemToResponse(TransferOrderItem item) {
        String productName = null;
        String barcode = null;
        if (item.getProductId() != null) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                productName = product.getProductName();
                barcode = product.getBarcode();
            }
        }

        return TransferOrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(productName)
                .barcode(barcode)
                .qty(item.getQty())
                .build();
    }
}
