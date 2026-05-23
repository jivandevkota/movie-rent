package org.example.movie_rental.repository;

import org.example.movie_rental.dto.FilmDto;
import org.example.movie_rental.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    // --- SECURE LIST VIEWS: DTO Projection (No Entity Fetch) ---

    @Query(value = """
        SELECT new org.example.movie_rental.dto.FilmDto(
            f.filmId,
            f.title,
            (SELECT c.name FROM Category c JOIN c.filmCategories fc WHERE fc.film.filmId = f.filmId),
            CONCAT('/img/films/', f.filmId, '.jpg')
        )
        FROM Film f
        """,
            countQuery = "SELECT COUNT(f) FROM Film f")
    Page<FilmDto> findAllFilmDtos(Pageable pageable);

    @Query(value = """
        SELECT new org.example.movie_rental.dto.FilmDto(
            f.filmId,
            f.title,
            (SELECT c2.name FROM Category c2 JOIN c2.filmCategories fc2 WHERE fc2.film.filmId = f.filmId),
            CONCAT('/img/films/', f.filmId, '.jpg')
        )
        FROM Film f
        JOIN f.filmCategories fc
        WHERE fc.category.categoryId = :categoryId
        """,
            countQuery = "SELECT COUNT(f) FROM Film f JOIN f.filmCategories fc WHERE fc.category.categoryId = :categoryId")
    Page<FilmDto> findFilmDtosByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = """
        SELECT new org.example.movie_rental.dto.FilmDto(
            f.filmId,
            f.title,
            (SELECT c2.name FROM Category c2 JOIN c2.filmCategories fc2 WHERE fc2.film.filmId = f.filmId),
            CONCAT('/img/films/', f.filmId, '.jpg')
        )
        FROM Film f
        WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))
        """,
            countQuery = "SELECT COUNT(f) FROM Film f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<FilmDto> searchFilmDtos(@Param("title") String title, Pageable pageable);

    // --- DETAIL HELPERS (Single record; entity OK here because we manually map to DTO) ---

    @Query("SELECT MIN(c.name) FROM Category c JOIN c.filmCategories fc WHERE fc.film.filmId = :filmId")
    String findCategoryNameByFilmId(@Param("filmId") Integer filmId);
}