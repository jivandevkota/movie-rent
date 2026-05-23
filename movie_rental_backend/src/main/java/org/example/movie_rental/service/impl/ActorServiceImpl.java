package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.ActorDetailDto;
import org.example.movie_rental.dto.ActorDto;
import org.example.movie_rental.dto.FilmSummaryDto;
import org.example.movie_rental.entity.Actor;
import org.example.movie_rental.exception.ResourceNotFoundException;
import org.example.movie_rental.repository.ActorRepository;
import org.example.movie_rental.repository.FilmActorRepository;
import org.example.movie_rental.service.ActorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final FilmActorRepository filmActorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ActorDto> getAllActors() {
        return actorRepository.findAll().stream()
                .map(this::toActorDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActorDetailDto getActorById(Integer actorId) {
        Actor actor = actorRepository.findByIdWithFilms(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", actorId));

        List<FilmSummaryDto> films = actor.getFilmActors().stream()
                .map(fa -> new FilmSummaryDto(
                        fa.getFilm().getFilmId(),
                        fa.getFilm().getTitle(),
                        "/img/films/" + fa.getFilm().getFilmId() + ".jpg"))
                .sorted(Comparator.comparing(FilmSummaryDto::title))
                .collect(Collectors.toList());

        return new ActorDetailDto(
                actor.getActorId(),
                actor.getFirstName() + " " + actor.getLastName(),
                "/img/actors/" + actor.getActorId() + ".jpg",
                films
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActorDto> getActorsByFilmId(Integer filmId) {
        return actorRepository.findActorsByFilmId(filmId).stream()
                .map(this::toActorDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ActorDto createActor(String firstName, String lastName) {
        Actor actor = Actor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .lastUpdate(LocalDateTime.now())
                .build();
        actor = actorRepository.save(actor);
        return toActorDto(actor);
    }

    @Override
    @Transactional
    public ActorDto updateActor(Integer actorId, String firstName, String lastName) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", actorId));
        if (firstName != null) actor.setFirstName(firstName);
        if (lastName != null) actor.setLastName(lastName);
        actor.setLastUpdate(LocalDateTime.now());
        actor = actorRepository.save(actor);
        return toActorDto(actor);
    }

    @Override
    @Transactional
    public void deleteActor(Integer actorId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor", actorId));
        if (actor.getFilmActors() != null) {
            filmActorRepository.deleteAll(actor.getFilmActors());
        }
        actorRepository.delete(actor);
    }

    private ActorDto toActorDto(Actor actor) {
        return new ActorDto(
                actor.getActorId(),
                actor.getFirstName() + " " + actor.getLastName(),
                "/img/actors/" + actor.getActorId() + ".jpg"
        );
    }
}
