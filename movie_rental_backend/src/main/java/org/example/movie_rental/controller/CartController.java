package org.example.movie_rental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.AddToCartRequest;
import org.example.movie_rental.dto.CartDto;
import org.example.movie_rental.dto.UpdateCartItemRequest;
import org.example.movie_rental.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/{customerId}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(@PathVariable Integer customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addToCart(
            @PathVariable Integer customerId,
            @Valid @RequestBody AddToCartRequest request) {
        CartDto cart = cartService.addToCart(customerId, request.filmId(),
                request.quantity() != null ? request.quantity() : 1);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable Integer customerId,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(customerId, cartItemId, request.quantity()));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> removeFromCart(
            @PathVariable Integer customerId,
            @PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(customerId, cartItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@PathVariable Integer customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
