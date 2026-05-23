package org.example.movie_rental.service;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface AdminRentalService {
    Page<Map<String, Object>> getAllRentals(int page, int size);
    Page<Map<String, Object>> getRentalsByStatus(int page, int size, String status);
}
