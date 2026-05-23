package org.example.movie_rental.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RentalOrderDto(
    Long orderId,
    Integer customerId,
    LocalDateTime orderDate,
    BigDecimal totalAmount,
    String status,
    List<OrderItemDto> items,
    List<CustomerRentalDto> rentals
) {}
