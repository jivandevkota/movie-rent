package org.example.movie_rental.dto;

public record FilmDto(
        Integer filmId,
        String title,
        String categoryName,
        String imageUrl
) {}