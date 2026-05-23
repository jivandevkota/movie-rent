package org.example.movie_rental.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
    Long cartId,
    Integer customerId,
    List<CartItemDto> items,
    BigDecimal totalAmount,
    int itemCount
) {}
