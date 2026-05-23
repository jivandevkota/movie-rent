package org.example.movie_rental.repository;

import org.example.movie_rental.entity.FilmCategory;
import org.example.movie_rental.entity.FilmCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId> {
}
