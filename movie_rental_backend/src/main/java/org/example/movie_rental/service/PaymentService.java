package org.example.movie_rental.service;

import org.example.movie_rental.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getPaymentsByCustomerId(Integer customerId);
}