package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CartDto;
import org.example.movie_rental.dto.CartItemDto;
import org.example.movie_rental.entity.*;
import org.example.movie_rental.exception.ResourceNotFoundException;
import org.example.movie_rental.repository.*;
import org.example.movie_rental.service.CartService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;

    @Override
    @Transactional(readOnly = true)
    public CartDto getCart(Integer customerId) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElse(null);
        if (cart == null) {
            return new CartDto(null, customerId, List.of(), BigDecimal.ZERO, 0);
        }
        return toCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto addToCart(Integer customerId, Integer filmId, Integer quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                return doAddToCart(customer, film, quantity);
            } catch (DataIntegrityViolationException e) {
                // Concurrent add: retry to merge with existing item
            }
        }
        throw new RuntimeException("Failed to add to cart after multiple attempts");
    }

    private CartDto doAddToCart(Customer customer, Film film, Integer quantity) {
        Integer customerId = customer.getCustomerId();
        Integer filmId = film.getFilmId();

        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .customer(customer)
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });

        List<CartItem> items = safeItems(cart);

        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getFilm().getFilmId().equals(filmId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .film(film)
                    .quantity(quantity)
                    .build();
            items.add(item);
        }

        cartRepository.save(cart);
        return toCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(Integer customerId, Long cartItemId, Integer quantity) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", customerId));

        CartItem item = safeItems(cart).stream()
                .filter(i -> i.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", cartItemId));

        item.setQuantity(quantity);
        cartRepository.save(cart);
        return toCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto removeFromCart(Integer customerId, Long cartItemId) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", customerId));

        safeItems(cart).removeIf(i -> i.getCartItemId().equals(cartItemId));
        cartRepository.save(cart);
        return toCartDto(cart);
    }

    @Override
    @Transactional
    public void clearCart(Integer customerId) {
        cartRepository.findByCustomerIdWithItems(customerId)
                .ifPresent(cart -> {
                    safeItems(cart).clear();
                    cartRepository.save(cart);
                });
    }

    private List<CartItem> safeItems(Cart cart) {
        List<CartItem> items = cart.getItems();
        if (items == null) {
            items = new ArrayList<>();
            cart.setItems(items);
        }
        return items;
    }

    private CartDto toCartDto(Cart cart) {
        List<CartItemDto> itemDtos = safeItems(cart).stream()
                .map(this::toCartItemDto)
                .toList();

        BigDecimal total = itemDtos.stream()
                .map(CartItemDto::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDto(
                cart.getCartId(),
                cart.getCustomer().getCustomerId(),
                itemDtos,
                total,
                itemDtos.size()
        );
    }

    private CartItemDto toCartItemDto(CartItem item) {
        Film film = item.getFilm();
        BigDecimal rate = BigDecimal.valueOf(film.getRentalRate());
        BigDecimal subtotal = rate.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemDto(
                item.getCartItemId(),
                film.getFilmId(),
                film.getTitle(),
                "/img/films/" + film.getFilmId() + ".jpg",
                item.getQuantity(),
                rate,
                subtotal,
                item.getAddedAt()
        );
    }
}
