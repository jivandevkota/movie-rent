package org.example.movie_rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 45) String firstName,
        @NotBlank @Size(max = 45) String lastName,
        @NotBlank @Email @Size(max = 50) String email,
        @NotBlank @Size(min = 4, max = 40) String password
) {}