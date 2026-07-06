package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.Driver.DriverStopResponse;
import com.smartdms.operation_service.dto.Driver.DriverSummaryResponse;
import com.smartdms.operation_service.dto.Driver.DriverVehicleInfoResponse;
import com.smartdms.operation_service.entity.*;
import com.smartdms.operation_service.repository.*;
import com.smartdms.operation_service.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverAssignmentRepository driverAssignmentRepository;
    private final VehicleRepository vehicleRepository;
    private final RoutePlanRepository routePlanRepository;
    private final TransferOrderRepository transferOrderRepository;
    private final CustomerRepository customerRepository;

    private static final String REQUEST   = "REQUEST";
    private static final String APPROVED  = "APPROVED";
    private static final String SHIPPED   = "SHIPPED";
    private static final String RECEIVED  = "RECEIVED";
    private static final String CANCELLED = "CANCELLED";
    @Override
    public List<DriverStopResponse> getStops(Long driverId, LocalDate date) {
        List<TransferOrder> orders = loadOrders(driverId, date);

        List<DriverStopResponse> stops = new java.util.ArrayList<>();
        int stopNo = 1;
        for (TransferOrder o : orders) {
            // items + qty
            int itemCount = 0;
            int totalQty = 0;
            if (o.getTransferOrderItems() != null) {
                itemCount = o.getTransferOrderItems().size();
                for (TransferOrderItem it : o.getTransferOrderItems()) {
                    totalQty += (it.getQty() == null ? 0 : it.getQty().intValue());
                }
            }

            // store name + address ពី customers (to_sd_id)
            String storeName = null, address = null;
            Double lat = null, lng = null;
            if (o.getToSdId() != null) {
                Customer c = customerRepository.findById(o.getToSdId()).orElse(null);
                if (c != null) {
                    storeName = c.getCustomerName();
                    address = c.getAddress();
                    lat = c.getLatitude();
                    lng = c.getLongitude();
                }
            }

            stops.add(DriverStopResponse.builder()
                    .stopNumber(stopNo++)
                    .transferOrderId(o.getId())
                    .storeName(storeName)
                    .address(address)
                    .latitude(lat)
                    .longitude(lng)
                    .status(mapStatus(o.getStatus()))
                    .rawStatus(o.getStatus())
                    .itemCount(itemCount)
                    .totalQty(totalQty)
                    .build());
        }
        return stops;
    }

    @Override
    public DriverSummaryResponse getSummary(Long driverId, LocalDate date) {
        List<TransferOrder> orders = loadOrders(driverId, date);

        long total = orders.size();
        long completed = orders.stream().filter(o -> "RECEIVED".equals(o.getStatus())).count();
        long inProgress = orders.stream().filter(o -> "SHIPPED".equals(o.getStatus())).count();
        long pending = orders.stream()
                .filter(o -> "REQUEST".equals(o.getStatus()) || "APPROVED".equals(o.getStatus()))
                .count();

        int percent = (total == 0) ? 0 : (int) Math.round((completed * 100.0) / total);

        return DriverSummaryResponse.builder()
                .totalStops(total)
                .completed(completed)
                .inProgress(inProgress)
                .pending(pending)
                .progressPercent(percent)
                .build();
    }

    @Override
    public DriverVehicleInfoResponse getVehicleInfo(Long driverId) {
        // យក assignment ថ្មីបំផុតរបស់ driver
        DriverAssignment assignment = driverAssignmentRepository.findByDriverId(driverId)
                .stream()
                .max((a, b) -> a.getId().compareTo(b.getId()))   // id ធំបំផុត = ថ្មីបំផុត
                .orElse(null);

        if (assignment == null) {
            return DriverVehicleInfoResponse.builder().build();   // driver គ្មាន assignment
        }

        // vehicle (plate + model)
        String plate = null, model = null;
        Long vehicleId = assignment.getVehicleId();
        if (vehicleId != null) {
            Vehicle v = vehicleRepository.findById(vehicleId).orElse(null);
            if (v != null) {
                plate = v.getVehicleNo();
                model = v.getModel();
            }
        }

        // route name (ប្រើ description; បើ null → "ROUTE-" + id)
        String routeName = null;
        Long routePlanId = assignment.getRoutePlanId();
        if (routePlanId != null) {
            RoutePlan rp = routePlanRepository.findById(routePlanId).orElse(null);
            if (rp != null) {
                routeName = (rp.getDescription() != null && !rp.getDescription().isBlank())
                        ? rp.getDescription()
                        : "ROUTE-" + rp.getId();
            }
        }

        return DriverVehicleInfoResponse.builder()
                .licensePlate(plate)
                .vehicleModel(model)
                .routeName(routeName)
                .vehicleId(vehicleId)
                .routePlanId(routePlanId)
                .driverAssignmentId(assignment.getId())
                .build();
    }
    // ---- helpers ----

    // ថ្ងៃ = 00:00 ដល់ 23:59:59 (ព្រោះ transfer_date ជា LocalDateTime)
    private List<TransferOrder> loadOrders(Long driverId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return transferOrderRepository
                .findByDriverIdAndTransferDateBetweenOrderByIdAsc(driverId, start, end);
    }

    // backend status → screen label
    private String mapStatus(String raw) {
        if (raw == null) return "Pending";
        return switch (raw) {
            case "RECEIVED" -> "Completed";
            case "SHIPPED"  -> "In Progress";
            case "CANCELLED"-> "Cancelled";
            default         -> "Pending";   // REQUEST, APPROVED
        };
    }
}