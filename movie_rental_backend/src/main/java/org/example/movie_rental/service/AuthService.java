package org.example.movie_rental.service;

import org.example.movie_rental.dto.LoginResponse;
import org.example.movie_rental.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(String email, String password);
    LoginResponse register(RegisterRequest request);
}
