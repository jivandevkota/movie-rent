package org.example.movie_rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "film_category")
@IdClass(FilmCategoryId.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FilmCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
