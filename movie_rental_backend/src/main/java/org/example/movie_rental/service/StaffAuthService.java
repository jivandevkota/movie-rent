package org.example.movie_rental.service;

import org.example.movie_rental.dto.StaffLoginResponse;

public interface StaffAuthService {
    StaffLoginResponse login(String email, String password);
}
