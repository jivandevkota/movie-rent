package org.example.movie_rental.dto;

public record LoginResponse(
        Integer customerId,
        String firstName,
        String lastName,
        String email
) {
}
