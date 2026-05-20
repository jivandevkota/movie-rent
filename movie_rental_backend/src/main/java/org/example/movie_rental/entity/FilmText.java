package org.example.movie_rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "film_text")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FilmText {

    @Id
    @Column(name = "film_id")
    private Integer filmId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
