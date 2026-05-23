package org.example.movie_rental.dto;

import java.util.List;

public record ActorDetailDto(
        Integer actorId,
        String name,
        String imageUrl,
        List<FilmSummaryDto> films
) {}
