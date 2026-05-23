package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.DashboardStatsDto;
import org.example.movie_rental.repository.CustomerRepository;
import org.example.movie_rental.repository.FilmRepository;
import org.example.movie_rental.repository.PaymentRepository;
import org.example.movie_rental.repository.RentalRepository;
import org.example.movie_rental.repository.StaffRepository;
import org.example.movie_rental.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final StaffRepository staffRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getStats() {
        long totalCustomers = customerRepository.count();
        long totalFilms = filmRepository.count();
        long activeRentals = rentalRepository.countActiveRentals();
        long overdueRentals = rentalRepository.countOverdueRentals();
        BigDecimal totalRevenue = paymentRepository.sumAllPayments();
        long totalStaff = staffRepository.count();

        return new DashboardStatsDto(
                totalCustomers,
                totalFilms,
                activeRentals,
                overdueRentals,
                totalRevenue != null ? totalRevenue : BigDecimal.ZERO,
                totalStaff
        );
    }
}
