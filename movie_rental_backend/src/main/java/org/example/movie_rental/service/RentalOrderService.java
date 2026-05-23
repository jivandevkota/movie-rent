package org.example.movie_rental.service;

import org.example.movie_rental.dto.RentalOrderDto;

import java.util.List;

public interface RentalOrderService {
    RentalOrderDto checkout(Integer customerId);
    List<RentalOrderDto> getOrdersByCustomerId(Integer customerId);
}
