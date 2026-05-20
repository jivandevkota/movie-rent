package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.FilmDetailsDto;
import org.example.movie_rental.dto.FilmDto;
import org.example.movie_rental.entity.Film;
import org.example.movie_rental.repository.FilmRepository;
import org.example.movie_rental.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;

    @Override
    @Transactional(readOnly = true)
    public FilmDetailsDto getFilmDetailsByFilmId(int filmId) {
        // Single-record fetch: entity is acceptable here because we manually map to DTO
        // and NEVER return the entity from the controller.
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new RuntimeException("Film not found: " + filmId));

        String categoryName = filmRepository.findCategoryNameByFilmId(filmId);
        List<String> actors = filmRepository.findActorsByFilmId(filmId);

        return new FilmDetailsDto(
                film.getFilmId(),
                film.getTitle(),
                film.getDescription(),
                categoryName != null ? categoryName : "Uncategorized",
                film.getReleaseYear(),
                film.getRentalDuration(),
                film.getRentalRate(),
                film.getLength(),
                film.getReplacementCost(),
                film.getRating(),
                film.getSpecialFeatures(),
                actors,
                "/img/films/" + film.getFilmId() + ".jpg"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilmDto> getAllFilms(int page, int size) {
        Pageable data = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "filmId"));
        return filmRepository.findAllFilmDtos(data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilmDto> getFilmByCategoryId(Long categoryId, int page, int size) {
        Pageable data = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "filmId"));
        return filmRepository.findFilmDtosByCategory(categoryId, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilmDto> searchFilmsByTitle(String title, int page, int size) {
        Pageable data = PageRequest.of(page, size, Sort.by("title").ascending());
        return filmRepository.searchFilmDtos(title, data);
    }
}