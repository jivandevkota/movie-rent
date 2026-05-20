package org.example.movie_rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "film_actor")
@IdClass(FilmActorId.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FilmActor {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
