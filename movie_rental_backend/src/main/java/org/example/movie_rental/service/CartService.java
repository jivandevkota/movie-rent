package org.example.movie_rental.service;

import org.example.movie_rental.dto.CartDto;

public interface CartService {
    CartDto getCart(Integer customerId);
    CartDto addToCart(Integer customerId, Integer filmId, Integer quantity);
    CartDto updateItemQuantity(Integer customerId, Long cartItemId, Integer quantity);
    CartDto removeFromCart(Integer customerId, Long cartItemId);
    void clearCart(Integer customerId);
}
