package com.smartdms.operation_service.service.impl;

import com.smartdms.operation_service.dto.saleskpi.SalesKpiDayResponse;
import com.smartdms.operation_service.dto.saleskpi.SalesKpiResponse;
import com.smartdms.operation_service.dto.saleskpi.SalesKpiStopResponse;
import com.smartdms.operation_service.entity.Customer;
import com.smartdms.operation_service.entity.CustomerCheckin;
import com.smartdms.operation_service.entity.RoutePlan;
import com.smartdms.operation_service.entity.RoutePlanDetail;
import com.smartdms.operation_service.entity.User;
import com.smartdms.operation_service.exception.ResourceNotFoundException;
import com.smartdms.operation_service.repository.CustomerCheckinRepository;
import com.smartdms.operation_service.repository.CustomerRepository;
import com.smartdms.operation_service.repository.RoutePlanRepository;
import com.smartdms.operation_service.repository.UserRepository;
import com.smartdms.operation_service.service.SalesKpiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SalesKpiServiceImpl implements SalesKpiService {

    private final UserRepository userRepository;
    private final RoutePlanRepository routePlanRepository;
    private final CustomerCheckinRepository customerCheckinRepository;
    private final CustomerRepository customerRepository;

    @Override
    public SalesKpiResponse getKpi(Long salesmanId, LocalDate startDate, LocalDate endDate) {

        User salesman = userRepository.findById(salesmanId)
                .orElseThrow(() -> new ResourceNotFoundException("Salesman not found with id: " + salesmanId));

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must not be before startDate");
        }

        // one route plan per salesman (enforced at creation) — take the first if present
        List<RoutePlanDetail> plannedStops = routePlanRepository.findBySalesmanId(salesmanId)
                .stream()
                .findFirst()
                .map(RoutePlan::getDetails)
                .orElse(List.of());

        LocalDateTime rangeStart = startDate.atStartOfDay();
        LocalDateTime rangeEnd = endDate.atTime(23, 59, 59);
        List<CustomerCheckin> checkins = customerCheckinRepository
                .findBySalesman_IdAndCheckinTimeBetween(salesmanId, rangeStart, rangeEnd);

        // earliest check-in per (calendar date, customer) — one visit per customer per day counts
        Map<LocalDate, Map<Long, LocalDateTime>> checkinsByDate = new HashMap<>();
        for (CustomerCheckin c : checkins) {
            LocalDate day = c.getCheckinTime().toLocalDate();
            checkinsByDate
                    .computeIfAbsent(day, d -> new HashMap<>())
                    .merge(c.getCustomer().getId(), c.getCheckinTime(),
                            (existing, candidate) -> candidate.isBefore(existing) ? candidate : existing);
        }

        List<SalesKpiDayResponse> days = new ArrayList<>();
        int totalPlanned = 0;
        int totalVisited = 0;

        for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
            String dayOfWeek = day.getDayOfWeek().name();

            List<RoutePlanDetail> plannedForDay = plannedStops.stream()
                    .filter(d -> dayOfWeek.equalsIgnoreCase(d.getDayOfWeek()))
                    .sorted(Comparator.comparing(d -> d.getSequenceNo() == null ? 0 : d.getSequenceNo()))
                    .toList();

            Map<Long, LocalDateTime> visitsThatDay = checkinsByDate.getOrDefault(day, Map.of());

            List<SalesKpiStopResponse> stops = new ArrayList<>();
            int visitedCount = 0;
            for (RoutePlanDetail detail : plannedForDay) {
                LocalDateTime checkinTime = visitsThatDay.get(detail.getCustomerId());
                boolean visited = checkinTime != null;
                if (visited) {
                    visitedCount++;
                }

                String customerName = customerRepository.findById(detail.getCustomerId())
                        .map(Customer::getCustomerName)
                        .orElse(null);

                stops.add(SalesKpiStopResponse.builder()
                        .customerId(detail.getCustomerId())
                        .customerName(customerName)
                        .sequenceNo(detail.getSequenceNo())
                        .visited(visited)
                        .checkinTime(checkinTime)
                        .build());
            }

            days.add(SalesKpiDayResponse.builder()
                    .date(day)
                    .dayOfWeek(dayOfWeek)
                    .plannedCount(plannedForDay.size())
                    .visitedCount(visitedCount)
                    .completionRate(rate(visitedCount, plannedForDay.size()))
                    .stops(stops)
                    .build());

            totalPlanned += plannedForDay.size();
            totalVisited += visitedCount;
        }

        // visits that happened but weren't on that day's plan (walk-ins / unplanned stops)
        int extraVisits = Math.max(0, checkins.size() - totalVisited);

        return SalesKpiResponse.builder()
                .salesmanId(salesmanId)
                .salesmanName(salesman.getFullName())
                .startDate(startDate)
                .endDate(endDate)
                .plannedVisits(totalPlanned)
                .completedVisits(totalVisited)
                .completionRate(rate(totalVisited, totalPlanned))
                .extraVisits(extraVisits)
                .days(days)
                .build();
    }

    private double rate(int visited, int planned) {
        if (planned == 0) {
            return 0.0;
        }
        return Math.round(visited * 1000.0 / planned) / 10.0; // one decimal place
    }
}
