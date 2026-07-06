package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Delivery.DeliveryRequest;
import com.smartdms.operation_service.dto.Delivery.DeliveryResponse;
import com.smartdms.operation_service.dto.stock.StockResponse;
import com.smartdms.operation_service.entity.*;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.*;
import com.smartdms.operation_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {


    private  final DeliveryRepository deliveryRepository;
    private  final UserRepository userRepository;
    private  final CustomerRepository customerRepository;
    private  final InvoiceRepository invoiceRepository;
    private  final ProductRepository productRepository;
    private  final VehicleRepository vehicleRepository;
    @Override
    public DeliveryResponse create(DeliveryRequest request) {
        String deliveryNo  = (request.getDeliveryNo() != null)
                && !request.getDeliveryNo().isBlank()
                ? request.getDeliveryNo() : "DLV-" + System.currentTimeMillis();

        if(deliveryRepository.existsByDeliveryNo(deliveryNo)){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,
                    "delivery_no already exists");
        }
        Delivery d = Delivery.builder()
                .deliveryNo(deliveryNo)
                .invoiceId(request.getInvoiceId())
                .customerId(request.getCustomerId())
                .driverId(request.getDriverId())          // 👈 THIS line must be here
                .vehicleId(request.getVehicleId())
                .routePlanId(request.getRoutePlanId())
                .driverAssignmentId(request.getDriverAssignmentId())
                .deliveryDate(request.getDeliveryDate() != null ? request.getDeliveryDate() : LocalDate.now())
                .status("PENDING")
                .remark(request.getRemark())
                .build();
        return toResponse(deliveryRepository.save(d));
    }

    @Override
    public DeliveryResponse update(Long id, DeliveryRequest request) {
        Delivery d  = load(id);

        if (request.getInvoiceId() != null) d.setInvoiceId(request.getInvoiceId());
        if (request.getCustomerId() != null) d.setCustomerId(request.getCustomerId());
        if (request.getDriverId() != null) d.setDriverId(request.getDriverId());
        if (request.getVehicleId() != null) d.setVehicleId(request.getVehicleId());
        if (request.getRoutePlanId() != null) d.setRoutePlanId(request.getRoutePlanId());
        if (request.getDriverAssignmentId() != null) d.setDriverAssignmentId(request.getDriverAssignmentId());
        if (request.getDeliveryDate() != null) d.setDeliveryDate(request.getDeliveryDate());
        if (request.getRemark() != null) d.setRemark(request.getRemark());

        return  toResponse(deliveryRepository.save(d));
    }

    @Override
    public DeliveryResponse getById(Long id) {
        return  toResponse(load(id));

    }

    @Override
    public List<DeliveryResponse> getByDriverAndDate(Long driverId, LocalDate date) {
        return  deliveryRepository.findByDriverIdAndDeliveryDateOrderByIdAsc(driverId,date)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public DeliveryResponse updateStatus(Long id, String status) {
        Delivery d = load(id);
        d.setStatus(status);
        LocalDateTime now = LocalDateTime.now();
        switch (status){
            case "STARTED" -> d.setStartedAt(now);
            case "ARRIVED" -> d.setArrivedAt(now);
            case "COMPLETED" -> d.setCompletedAt(now);
            default -> { /* PENDING / FAILED: no timestamp */ }

        }
        return  toResponse(deliveryRepository.save(d));

    }

    @Override
    public void delete(Long id) {

        deliveryRepository.delete(load(id));

    }

    private  Delivery load(Long id){
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Delivery not found"));
    }


    private DeliveryResponse toResponse(Delivery d) {

        // Look up related names (null-safe: if id is null or not found, name stays null)
        String fullName = (d.getDriverId() == null) ? null :
                userRepository.findById(d.getDriverId())
                        .map(User::getFullName).orElse(null);

        String customerName = (d.getCustomerId() == null) ? null :
                customerRepository.findById(d.getCustomerId())
                        .map(Customer::getCustomerName).orElse(null);          // change getName() to your column

        String invoiceNo = (d.getInvoiceId() == null) ? null :
                invoiceRepository.findById(d.getInvoiceId())
                        .map(Invoice::getInvoiceNo).orElse(null);      // change getInvoiceNo() to your column

        String vehicleNo = (d.getVehicleId() == null)? null :
                vehicleRepository.findById(d.getVehicleId())
                .map(Vehicle::getVehicleNo).orElse(null);
        return DeliveryResponse.builder()
                .id(d.getId())
                .deliveryNo(d.getDeliveryNo())
                .invoiceId(d.getInvoiceId())
                .invoiceNo(invoiceNo)                                  // 👈 name
                .customerId(d.getCustomerId())
                .customerName(customerName)                            // 👈 name
                .driverId(d.getDriverId())
                .fullName(fullName)                                    // 👈 driver name
                .vehicleId(d.getVehicleId())
                 .vehicleNo(vehicleNo)                               // add if you have VehicleRepository
                .routePlanId(d.getRoutePlanId())
                .driverAssignmentId(d.getDriverAssignmentId())
                .deliveryDate(d.getDeliveryDate())
                .status(d.getStatus())
                .startedAt(d.getStartedAt())
                .arrivedAt(d.getArrivedAt())
                .completedAt(d.getCompletedAt())
                .latitude(d.getLatitude())
                .longitude(d.getLongitude())
                .remark(d.getRemark())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }


}
