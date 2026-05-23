package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.movie_rental.dto.CategoryDto;
import org.example.movie_rental.dto.FilmDetailsDto;
import org.example.movie_rental.dto.FilmDto;
import org.example.movie_rental.service.CategoryService;
import org.example.movie_rental.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final FilmService filmService;
    private final CategoryService categoryService;
    private final EntityManager em;

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

    @GetMapping("/films/popular")
    public ResponseEntity<List<Map<String, Object>>> getPopularFilms() {
        Query query = em.createQuery(
                "SELECT f.filmId, f.title, COUNT(r) as cnt " +
                "FROM Film f JOIN f.inventories i JOIN i.rentals r " +
                "GROUP BY f.filmId ORDER BY cnt DESC");
        query.setMaxResults(8);
        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> data = results.stream()
                .map(row -> Map.<String, Object>of(
                        "filmId", row[0],
                        "title", row[1],
                        "rentalCount", row[2],
                        "imageUrl", "/img/films/" + row[0] + ".jpg"))
                .toList();
        return ResponseEntity.ok(data);
    }
}
