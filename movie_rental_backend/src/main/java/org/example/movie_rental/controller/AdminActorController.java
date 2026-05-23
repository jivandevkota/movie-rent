package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.ActorDetailDto;
import org.example.movie_rental.dto.ActorDto;
import org.example.movie_rental.service.ActorService;
import org.example.movie_rental.service.EnhancedDashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/actors")
@RequiredArgsConstructor
public class AdminActorController {

    private final ActorService actorService;
    private final EnhancedDashboardService enhancedDashboardService;

    @GetMapping
    public ResponseEntity<List<ActorDto>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDetailDto> getActor(@PathVariable Integer id) {
        return ResponseEntity.ok(actorService.getActorById(id));
    }

    @PostMapping
    public ResponseEntity<ActorDto> createActor(@RequestBody Map<String, String> body) {
        ActorDto actor = actorService.createActor(body.get("firstName"), body.get("lastName"));
        return ResponseEntity.status(HttpStatus.CREATED).body(actor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorDto> updateActor(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(actorService.updateActor(id, body.get("firstName"), body.get("lastName")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getActorStats(@PathVariable Integer id) {
        return ResponseEntity.ok(enhancedDashboardService.getActorStats(id));
    }
}
