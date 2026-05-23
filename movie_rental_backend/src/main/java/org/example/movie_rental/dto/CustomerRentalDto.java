package org.example.movie_rental.dto;

import java.time.LocalDateTime;

public record CustomerRentalDto(
        Integer rentalId,
        Integer filmId,
        String filmTitle,
        String imageUrl,
        LocalDateTime rentalDate,
        LocalDateTime returnDate,
        boolean active
) {
    public CustomerRentalDto(Integer rentalId, Integer filmId, String filmTitle, String imageUrl,
                             LocalDateTime rentalDate, LocalDateTime returnDate) {
        this(rentalId, filmId, filmTitle, imageUrl, rentalDate, returnDate, returnDate == null);
    }
}
