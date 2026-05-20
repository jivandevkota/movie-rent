package org.example.movie_rental.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class FilmCategoryId implements Serializable {

    private Integer film;
    private Integer category;
}
