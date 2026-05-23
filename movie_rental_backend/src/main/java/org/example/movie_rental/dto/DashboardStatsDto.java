package org.example.movie_rental.dto;

import java.math.BigDecimal;

public record DashboardStatsDto(
        long totalCustomers,
        long totalFilms,
        long activeRentals,
        long overdueRentals,
        BigDecimal totalRevenue,
        long totalStaff
) {}
