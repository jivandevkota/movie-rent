package org.example.movie_rental.dto;

public record StaffLoginResponse(
        Integer staffId,
        String firstName,
        String lastName,
        String email,
        boolean active
) {}
