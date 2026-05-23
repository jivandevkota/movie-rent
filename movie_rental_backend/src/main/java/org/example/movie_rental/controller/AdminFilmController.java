package org.example.movie_rental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.*;
import org.example.movie_rental.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminFilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public ResponseEntity<Page<FilmDetailDto>> getAllFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(filmService.getAllFilmDetailsForAdmin(page, size));
    }

    @GetMapping("/films/{filmId}")
    public ResponseEntity<FilmDetailDto> getFilm(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getFilmDetailForAdmin(filmId));
    }

    @PostMapping("/films")
    public ResponseEntity<FilmDetailDto> createFilm(@Valid @RequestBody CreateFilmRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(request));
    }

    @PutMapping("/films/{filmId}")
    public ResponseEntity<FilmDetailDto> updateFilm(
            @PathVariable Integer filmId,
            @Valid @RequestBody CreateFilmRequest request) {
        return ResponseEntity.ok(filmService.updateFilm(filmId, request));
    }

    @DeleteMapping("/films/{filmId}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/films/{filmId}/inventory")
    public ResponseEntity<List<InventoryDto>> getInventory(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getFilmInventory(filmId));
    }

    @PostMapping("/films/{filmId}/inventory")
    public ResponseEntity<InventoryDto> addInventory(
            @PathVariable Integer filmId,
            @RequestParam(defaultValue = "1") Integer storeId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(filmService.addInventoryCopy(filmId, storeId));
    }

    @DeleteMapping("/films/inventory/{inventoryId}")
    public ResponseEntity<Void> removeInventory(@PathVariable Integer inventoryId) {
        filmService.removeInventoryCopy(inventoryId);
        return ResponseEntity.noContent().build();
    }
}
