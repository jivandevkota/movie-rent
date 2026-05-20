package org.example.movie_rental.dto;

import java.util.List;

public record FilmDetailsDto(
        Integer filmId,
        String title,
        String description,
        String categoryName,
        Integer releaseYear,
        Integer rentalDuration,
        Float rentalRate,
        Integer length,
        Float replacementCost,
        String rating,
        String specialFeatures,
        List<String> actors,
        String imageUrl
) {}