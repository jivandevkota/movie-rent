package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.RentalOrderDto;
import org.example.movie_rental.service.RentalOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/orders")
@RequiredArgsConstructor
public class RentalOrderController {

    private final RentalOrderService rentalOrderService;

    @PostMapping("/checkout")
    public ResponseEntity<RentalOrderDto> checkout(@PathVariable Integer customerId) {
        RentalOrderDto order = rentalOrderService.checkout(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public ResponseEntity<List<RentalOrderDto>> getOrders(@PathVariable Integer customerId) {
        return ResponseEntity.ok(rentalOrderService.getOrdersByCustomerId(customerId));
    }
}
