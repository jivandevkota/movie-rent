package org.example.movie_rental.service;

import org.example.movie_rental.dto.FilmDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FilmServiceTest {

    @Autowired
    private FilmService filmService;

    @Test
    void testGetFilmDetailsByFilmId() {
       FilmDetailsDto details = filmService.getFilmDetailsByFilmId(1);
        System.out.println("\n=== FILM DETAILS FOR filmId=1 ===");
        System.out.println("Title: " + details.title());
        System.out.println("Description: " + details.description());
        System.out.println("Category: " + details.categoryName());
        System.out.println("Release Year: " + details.releaseYear());
        System.out.println("Rental Duration: " + details.rentalDuration());
        System.out.println("Rental Rate: " + details.rentalRate());
        System.out.println("Length: " + details.length());
    }
}
