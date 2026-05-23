package org.example.movie_rental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.StaffLoginRequest;
import org.example.movie_rental.dto.StaffLoginResponse;
import org.example.movie_rental.service.StaffAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffAuthController {

    private final StaffAuthService staffAuthService;

    @PostMapping("/login")
    public ResponseEntity<StaffLoginResponse> login(@Valid @RequestBody StaffLoginRequest request) {
        StaffLoginResponse response = staffAuthService.login(request.email(), request.password());
        return ResponseEntity.ok(response);
    }
}
