package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.*;
import org.example.movie_rental.entity.*;
import org.example.movie_rental.exception.BusinessException;
import org.example.movie_rental.exception.ResourceNotFoundException;
import org.example.movie_rental.repository.*;
import org.example.movie_rental.service.RentalOrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalOrderServiceImpl implements RentalOrderService {

    @Value("${app.online-staff.email}")
    private String onlineStaffEmail;

    private final CartRepository cartRepository;
    private final RentalOrderRepository rentalOrderRepository;
    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;
    private final RentalRepository rentalRepository;
    private final StaffRepository staffRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public RentalOrderDto checkout(Integer customerId) {
        Cart cart = cartRepository.findByCustomerIdWithItems(customerId)
                .orElseThrow(() -> new BusinessException("Cart is empty"));

        List<CartItem> cartItems = cart.getItems();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new BusinessException("Cart is empty");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));

        Staff staff = staffRepository.findByEmail(onlineStaffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Online staff not found: " + onlineStaffEmail));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Integer, List<Inventory>> inventoryCache = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Film film = cartItem.getFilm();
            int quantity = cartItem.getQuantity();
            BigDecimal unitPrice = BigDecimal.valueOf(film.getRentalRate());
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(subtotal);

            List<Inventory> available = rentalRepository.findAvailableInventory(
                    film.getFilmId(), PageRequest.of(0, quantity));

            if (available.size() < quantity) {
                throw new BusinessException(
                        "Only " + available.size() + " copy(s) of \"" + film.getTitle()
                        + "\" available, but requested " + quantity);
            }

            inventoryCache.put(film.getFilmId(), available);

            OrderItem orderItem = OrderItem.builder()
                    .film(film)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();
            orderItems.add(orderItem);
        }

        RentalOrder order = RentalOrder.builder()
                .customer(customer)
                .totalAmount(totalAmount)
                .status("COMPLETED")
                .items(orderItems)
                .build();

        for (OrderItem oi : orderItems) {
            oi.setOrder(order);
        }

        order = rentalOrderRepository.save(order);

        List<CustomerRentalDto> rentalDtos = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Film film = cartItem.getFilm();
            int quantity = cartItem.getQuantity();
            List<Inventory> available = inventoryCache.get(film.getFilmId());
            LocalDateTime now = LocalDateTime.now();

            for (int i = 0; i < quantity; i++) {
                Inventory inv = available.get(i);

                Rental rental = Rental.builder()
                        .rentalDate(now)
                        .inventory(inv)
                        .customer(customer)
                        .staff(staff)
                        .order(order)
                        .lastUpdate(now)
                        .build();

                rental = rentalRepository.save(rental);

                Payment payment = Payment.builder()
                        .customer(customer)
                        .staff(staff)
                        .rental(rental)
                        .amount(BigDecimal.valueOf(film.getRentalRate()))
                        .paymentDate(now)
                        .lastUpdate(now)
                        .build();

                paymentRepository.save(payment);

                rentalDtos.add(new CustomerRentalDto(
                        rental.getRentalId(),
                        film.getFilmId(),
                        film.getTitle(),
                        "/img/films/" + film.getFilmId() + ".jpg",
                        rental.getRentalDate(),
                        null,
                        true
                ));
            }
        }

        cartItems.clear();
        cartRepository.save(cart);

        return toOrderDto(order, rentalDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalOrderDto> getOrdersByCustomerId(Integer customerId) {
        List<RentalOrder> orders = rentalOrderRepository.findByCustomerIdWithItems(customerId);
        return orders.stream()
                .map(o -> toOrderDto(o, getRentalsForOrder(o)))
                .collect(Collectors.toList());
    }

    private List<CustomerRentalDto> getRentalsForOrder(RentalOrder order) {
        if (order.getRentals() == null) return List.of();
        return order.getRentals().stream()
                .map(r -> new CustomerRentalDto(
                        r.getRentalId(),
                        r.getInventory().getFilm().getFilmId(),
                        r.getInventory().getFilm().getTitle(),
                        "/img/films/" + r.getInventory().getFilm().getFilmId() + ".jpg",
                        r.getRentalDate(),
                        r.getReturnDate()
                ))
                .collect(Collectors.toList());
    }

    private RentalOrderDto toOrderDto(RentalOrder order, List<CustomerRentalDto> rentals) {
        List<OrderItemDto> itemDtos = (order.getItems() == null ? List.<OrderItem>of() : order.getItems())
                .stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());

        return new RentalOrderDto(
                order.getOrderId(),
                order.getCustomer().getCustomerId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                itemDtos,
                rentals
        );
    }

    private OrderItemDto toOrderItemDto(OrderItem item) {
        return new OrderItemDto(
                item.getOrderItemId(),
                item.getFilm().getFilmId(),
                item.getFilm().getTitle(),
                "/img/films/" + item.getFilm().getFilmId() + ".jpg",
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
}
