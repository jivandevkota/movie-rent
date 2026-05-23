package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CustomerRentalDto;
import org.example.movie_rental.dto.RentRequest;
import org.example.movie_rental.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRentalController {

    private final RentalService rentalService;

    @GetMapping("/{customerId}/rentals")
    public ResponseEntity<List<CustomerRentalDto>> getCustomerRentals(@PathVariable Integer customerId) {
        List<CustomerRentalDto> rentals = rentalService.getRentalsByCustomerId(customerId);
        return ResponseEntity.ok(rentals);
    }

    @PostMapping("/{customerId}/rentals")
    public ResponseEntity<CustomerRentalDto> rentFilm(
            @PathVariable Integer customerId,
            @RequestBody RentRequest request) {
        CustomerRentalDto rental = rentalService.rentFilm(customerId, request.filmId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PutMapping("/{customerId}/rentals/{rentalId}/return")
    public ResponseEntity<CustomerRentalDto> returnFilm(
            @PathVariable Integer customerId,
            @PathVariable Integer rentalId) {
        CustomerRentalDto rental = rentalService.returnFilm(customerId, rentalId);
        return ResponseEntity.ok(rental);
    }
}
