package org.example.movie_rental.service;

import org.example.movie_rental.dto.ActorDetailDto;
import org.example.movie_rental.dto.ActorDto;

import java.util.List;

public interface ActorService {
    List<ActorDto> getAllActors();
    ActorDetailDto getActorById(Integer actorId);
    List<ActorDto> getActorsByFilmId(Integer filmId);
    ActorDto createActor(String firstName, String lastName);
    ActorDto updateActor(Integer actorId, String firstName, String lastName);
    void deleteActor(Integer actorId);
}
