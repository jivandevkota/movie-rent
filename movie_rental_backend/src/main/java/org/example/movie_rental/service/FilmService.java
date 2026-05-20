package org.example.movie_rental.service;

import org.example.movie_rental.dto.FilmDetailsDto;
import org.example.movie_rental.dto.FilmDto;
import org.springframework.data.domain.Page;

public interface FilmService {
    FilmDetailsDto getFilmDetailsByFilmId(int filmId);
    Page<FilmDto> getAllFilms(int page, int size);
    Page<FilmDto> getFilmByCategoryId(Long categoryId, int page, int size);
    Page<FilmDto> searchFilmsByTitle(String title, int page, int size);
}