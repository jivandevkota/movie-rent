package org.example.movie_rental.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class FilmActorId implements Serializable {

    private Integer actor;
    private Integer film;
}
