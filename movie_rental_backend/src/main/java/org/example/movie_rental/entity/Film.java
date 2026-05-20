package org.example.movie_rental.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "film")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "language", "originalLanguage", "filmActors", "filmCategories", "inventories"})
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Integer filmId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_language_id")
    private Language originalLanguage;

    @Column(name = "rental_duration")
    private Integer rentalDuration;

    @Column(name = "rental_rate")
    private Float rentalRate;

    @Column(name = "length")
    private Integer length;

    @Column(name = "replacement_cost")
    private Float replacementCost;

    @Column(name = "rating", length = 255)
    private String rating;

    @Column(name = "special_features", length = 255)
    private String specialFeatures;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @Transient
    private String imageUrl;

    @Transient
    private String actors;

    @Transient
    private String category;

    @Transient
    private Integer count;

    @Transient
    private Long categoryId;

    @OneToMany(mappedBy = "film")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FilmActor> filmActors;

    @OneToMany(mappedBy = "film")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FilmCategory> filmCategories;

    @OneToMany(mappedBy = "film")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Inventory> inventories;
}
