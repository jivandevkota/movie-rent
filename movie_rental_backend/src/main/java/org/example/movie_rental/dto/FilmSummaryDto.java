package org.example.movie_rental.dto;

public record FilmSummaryDto(
        Integer filmId,
        String title,
        String imageUrl
) {}
