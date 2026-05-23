package org.example.movie_rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
    @NotNull Integer filmId,
    @Min(1) Integer quantity
) {}
