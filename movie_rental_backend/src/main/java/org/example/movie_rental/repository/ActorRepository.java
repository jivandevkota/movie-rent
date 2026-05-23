package org.example.movie_rental.repository;

import org.example.movie_rental.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends JpaRepository<Actor, Integer> {

    List<Actor> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    @Query("SELECT a FROM Actor a JOIN a.filmActors fa WHERE fa.film.filmId = :filmId ORDER BY a.lastName, a.firstName")
    List<Actor> findActorsByFilmId(@Param("filmId") Integer filmId);

    @Query("SELECT a FROM Actor a LEFT JOIN FETCH a.filmActors fa LEFT JOIN FETCH fa.film f WHERE a.actorId = :actorId")
    Optional<Actor> findByIdWithFilms(@Param("actorId") Integer actorId);
}
