package org.example.movie_rental.dto;

import java.math.BigDecimal;

public record OrderItemDto(
    Long orderItemId,
    Integer filmId,
    String filmTitle,
    String imageUrl,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {}
