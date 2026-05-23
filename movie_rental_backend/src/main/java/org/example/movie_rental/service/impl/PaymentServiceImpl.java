package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.PaymentDto;
import org.example.movie_rental.entity.Payment;
import org.example.movie_rental.repository.PaymentRepository;
import org.example.movie_rental.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByCustomerId(Integer customerId) {
        return paymentRepository.findByCustomerCustomerIdOrderByPaymentDateDesc(customerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PaymentDto toDto(Payment payment) {
        String filmTitle = payment.getRental() != null
                ? payment.getRental().getInventory().getFilm().getTitle()
                : "N/A";

        return new PaymentDto(
                payment.getPaymentId(),
                payment.getRental() != null ? payment.getRental().getRentalId() : null,
                filmTitle,
                payment.getAmount(),
                payment.getPaymentDate()
        );
    }
}