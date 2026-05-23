package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CustomerRentalDto;
import org.example.movie_rental.service.AdminRentalService;
import org.example.movie_rental.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/rentals")
@RequiredArgsConstructor
public class AdminRentalController {

    private final AdminRentalService adminRentalService;
    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<Map<String, Object>>> getAllRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "all") String status) {
        if ("all".equals(status)) {
            return ResponseEntity.ok(adminRentalService.getAllRentals(page, size));
        }
        return ResponseEntity.ok(adminRentalService.getRentalsByStatus(page, size, status));
    }

    @PutMapping("/{rentalId}/return")
    public ResponseEntity<Void> processReturn(@PathVariable Integer rentalId) {
        rentalService.adminReturnFilm(rentalId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<CustomerRentalDto> processRental(
            @RequestParam Integer customerId,
            @RequestParam Integer filmId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rentalService.rentFilm(customerId, filmId));
    }
}
