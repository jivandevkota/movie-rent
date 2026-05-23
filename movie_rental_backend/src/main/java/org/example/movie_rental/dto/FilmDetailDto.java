package org.example.movie_rental.dto;

import java.math.BigDecimal;
import java.util.List;

public record FilmDetailDto(
        Integer filmId,
        String title,
        String description,
        Integer releaseYear,
        Integer rentalDuration,
        BigDecimal rentalRate,
        Integer length,
        BigDecimal replacementCost,
        String rating,
        String specialFeatures,
        Integer languageId,
        String languageName,
        Integer categoryId,
        String categoryName,
        List<ActorDto> actors,
        long availableCopies,
        long totalCopies
) {}
