package org.example.movie_rental.service;

import org.example.movie_rental.dto.*;
import org.springframework.data.domain.Page;
import java.util.List;

public interface FilmService {
    FilmDetailsDto getFilmDetailsByFilmId(int filmId);
    Page<FilmDto> getAllFilms(int page, int size);
    Page<FilmDto> getFilmByCategoryId(Long categoryId, int page, int size);
    Page<FilmDto> searchFilmsByTitle(String title, int page, int size);

    FilmDetailDto getFilmDetailForAdmin(Integer filmId);
    FilmDetailDto createFilm(CreateFilmRequest request);
    FilmDetailDto updateFilm(Integer filmId, CreateFilmRequest request);
    void deleteFilm(Integer filmId);
    List<InventoryDto> getFilmInventory(Integer filmId);
    InventoryDto addInventoryCopy(Integer filmId, Integer storeId);
    void removeInventoryCopy(Integer inventoryId);
    List<CategoryDto> getAllCategoriesForAdmin();
    org.springframework.data.domain.Page<FilmDetailDto> getAllFilmDetailsForAdmin(int page, int size);
}