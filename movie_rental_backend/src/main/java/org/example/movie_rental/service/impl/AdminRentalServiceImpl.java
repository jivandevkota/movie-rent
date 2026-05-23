package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.repository.RentalRepository;
import org.example.movie_rental.service.AdminRentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminRentalServiceImpl implements AdminRentalService {

    private final RentalRepository rentalRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getAllRentals(int page, int size) {
        return fetchRentals(rentalRepository.findAllRentalData(PageRequest.of(page, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getRentalsByStatus(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        return switch (status) {
            case "active" -> fetchRentals(rentalRepository.findActiveRentals(pageable));
            case "returned" -> fetchRentals(rentalRepository.findReturnedRentals(pageable));
            case "overdue" -> fetchRentals(rentalRepository.findOverdueRentals(pageable));
            default -> fetchRentals(rentalRepository.findAllRentalData(pageable));
        };
    }

    private Page<Map<String, Object>> fetchRentals(Page<Object[]> data) {
        return data.map(row -> {
            Object[] r = (Object[]) row;
            LocalDateTime rentalLdt = (LocalDateTime) r[6];
            LocalDateTime returnLdt = (LocalDateTime) r[7];
            int rentalDuration = (Integer) r[8];

            boolean active = returnLdt == null;
            boolean overdue = active && Duration.between(rentalLdt, LocalDateTime.now()).toDays() > rentalDuration;

            Map<String, Object> map = new HashMap<>();
            map.put("rentalId", r[0]);
            map.put("customerId", r[1]);
            map.put("customerName", r[2] + " " + r[3]);
            map.put("filmId", r[4]);
            map.put("filmTitle", r[5]);
            map.put("rentalDate", rentalLdt);
            map.put("returnDate", returnLdt);
            map.put("active", active);
            map.put("overdue", overdue);
            return map;
        });
    }
}
