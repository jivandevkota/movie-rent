package org.example.movie_rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateFilmRequest(
        @NotBlank @Size(max = 255) String title,
        String description,
        @NotNull Integer releaseYear,
        @NotNull @Positive Integer rentalDuration,
        @NotNull @Positive BigDecimal rentalRate,
        Integer length,
        @NotNull @Positive BigDecimal replacementCost,
        String rating,
        String specialFeatures,
        @NotNull Integer languageId,
        Integer originalLanguageId,
        @NotNull Integer categoryId
) {}
