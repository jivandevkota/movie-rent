package org.example.movie_rental.service;

import org.example.movie_rental.dto.CustomerRentalDto;

import java.util.List;

public interface RentalService {
    List<CustomerRentalDto> getRentalsByCustomerId(Integer customerId);
    CustomerRentalDto rentFilm(Integer customerId, Integer filmId);
    CustomerRentalDto returnFilm(Integer customerId, Integer rentalId);
    CustomerRentalDto adminReturnFilm(Integer rentalId);
}
