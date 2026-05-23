package org.example.movie_rental.dto;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequest(
    @Min(1) Integer quantity
) {}
