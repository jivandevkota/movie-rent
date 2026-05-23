package org.example.movie_rental.repository;

import org.example.movie_rental.entity.FilmActor;
import org.example.movie_rental.entity.FilmActorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {
}
