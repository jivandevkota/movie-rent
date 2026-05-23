package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CustomerRentalDto;
import org.example.movie_rental.entity.*;
import org.example.movie_rental.exception.BusinessException;
import org.example.movie_rental.exception.ResourceNotFoundException;
import org.example.movie_rental.repository.*;
import org.example.movie_rental.service.RentalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    @Value("${app.online-staff.email}")
    private String onlineStaffEmail;

    private final RentalRepository rentalRepository;
    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;
    private final StaffRepository staffRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerRentalDto> getRentalsByCustomerId(Integer customerId) {
        return rentalRepository.findRentalsByCustomerId(customerId);
    }

    @Override
    @Transactional
    public CustomerRentalDto returnFilm(Integer customerId, Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", rentalId));

        if (!rental.getCustomer().getCustomerId().equals(customerId)) {
            throw new BusinessException("Rental does not belong to this customer");
        }

        return doReturn(rental);
    }

    @Override
    @Transactional
    public CustomerRentalDto adminReturnFilm(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", rentalId));
        return doReturn(rental);
    }

    private CustomerRentalDto doReturn(Rental rental) {
        if (rental.getReturnDate() != null) {
            throw new BusinessException("Film already returned");
        }

        LocalDateTime now = LocalDateTime.now();
        rental.setReturnDate(now);
        rental.setLastUpdate(now);
        rental = rentalRepository.save(rental);

        String imageUrl = "/img/films/" + rental.getInventory().getFilm().getFilmId() + ".jpg";

        return new CustomerRentalDto(
                rental.getRentalId(),
                rental.getInventory().getFilm().getFilmId(),
                rental.getInventory().getFilm().getTitle(),
                imageUrl,
                rental.getRentalDate(),
                rental.getReturnDate(),
                false
        );
    }

    @Override
    @Transactional
    public CustomerRentalDto rentFilm(Integer customerId, Integer filmId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        List<Inventory> available = rentalRepository.findAvailableInventory(
                filmId, PageRequest.of(0, 1));

        if (available.isEmpty()) {
            throw new BusinessException("No copies of \"" + film.getTitle() + "\" available for rent");
        }

        Inventory inventory = available.get(0);
        Staff staff = staffRepository.findByEmail(onlineStaffEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Online staff not found: " + onlineStaffEmail));

        LocalDateTime now = LocalDateTime.now();

        Rental rental = Rental.builder()
                .rentalDate(now)
                .inventory(inventory)
                .customer(customer)
                .staff(staff)
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

        String imageUrl = "/img/films/" + filmId + ".jpg";

        return new CustomerRentalDto(
                rental.getRentalId(),
                filmId,
                film.getTitle(),
                imageUrl,
                rental.getRentalDate(),
                null,
                true
        );
    }
}
