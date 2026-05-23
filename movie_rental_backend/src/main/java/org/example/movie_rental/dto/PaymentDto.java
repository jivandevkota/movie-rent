package org.example.movie_rental.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Integer paymentId,
        Integer rentalId,
        String filmTitle,
        BigDecimal amount,
        LocalDateTime paymentDate
) {}