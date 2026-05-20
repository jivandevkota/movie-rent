package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CategoryDto;
import org.example.movie_rental.dto.FilmDetailsDto;
import org.example.movie_rental.dto.FilmDto;
import org.example.movie_rental.entity.Film;
import org.example.movie_rental.service.CategoryService;
import org.example.movie_rental.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final FilmService filmService;
    private final CategoryService categoryService;

    @GetMapping("/films")
    public ResponseEntity<Page<FilmDto>> getAllFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<FilmDto> filmDtos = filmService.getAllFilms(page, size);
        return ResponseEntity.ok().body(filmDtos);
    }

    @GetMapping("/category/{categoryId}/films")
    public ResponseEntity<Page<FilmDto>> getFilmByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<FilmDto> filmDtos = filmService.getFilmByCategoryId(categoryId, page, size);
        return ResponseEntity.ok().body(filmDtos);
    }

    @GetMapping("/films/search")
    public ResponseEntity<Page<FilmDto>> getFilmBySearchTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<FilmDto> filmDtos = filmService.searchFilmsByTitle(title, page, size);
        return ResponseEntity.ok().body(filmDtos);
    }

    @GetMapping("/films/{filmId}")
    public ResponseEntity<FilmDetailsDto> getFilmDetailsById(@PathVariable Integer filmId) {
      FilmDetailsDto filmDetails = filmService.getFilmDetailsByFilmId(filmId);
        return ResponseEntity.ok().body(filmDetails);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtos = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categoryDtos);
    }
}
