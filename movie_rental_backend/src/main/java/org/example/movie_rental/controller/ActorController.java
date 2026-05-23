package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.ActorDetailDto;
import org.example.movie_rental.dto.ActorDto;
import org.example.movie_rental.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<ActorDto>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDetailDto> getActorById(@PathVariable Integer id) {
        return ResponseEntity.ok(actorService.getActorById(id));
    }
}
