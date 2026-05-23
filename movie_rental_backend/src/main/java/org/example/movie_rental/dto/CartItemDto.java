package org.example.movie_rental.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CartItemDto(
    Long cartItemId,
    Integer filmId,
    String filmTitle,
    String imageUrl,
    Integer quantity,
    BigDecimal rentalRate,
    BigDecimal subtotal,
    LocalDateTime addedAt
) {}
