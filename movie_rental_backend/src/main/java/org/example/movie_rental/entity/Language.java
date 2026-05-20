package org.example.movie_rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "language")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Integer languageId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "language")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Film> films;

    @OneToMany(mappedBy = "originalLanguage")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Film> originalLanguageFilms;
}
