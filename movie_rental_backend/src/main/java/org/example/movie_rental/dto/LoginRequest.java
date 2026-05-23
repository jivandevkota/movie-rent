package org.example.movie_rental.dto;

public record LoginRequest(
        String email,
        String password
) {
}
