package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.LoginResponse;
import org.example.movie_rental.dto.RegisterRequest;
import org.example.movie_rental.entity.*;
import org.example.movie_rental.exception.BusinessException;
import org.example.movie_rental.repository.*;
import org.example.movie_rental.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;

    @Override
    public LoginResponse login(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!password.equals(customer.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        return toLoginResponse(customer);
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException("Email already registered");
        }

        Address defaultAddress = addressRepository.getReferenceById(1);
        Store defaultStore = storeRepository.getReferenceById(1);
        LocalDateTime now = LocalDateTime.now();

        Customer customer = Customer.builder()
                .store(defaultStore)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .address(defaultAddress)
                .active(true)
                .password(request.password())
                .createDate(now)
                .lastUpdate(now)
                .build();

        customer = customerRepository.save(customer);
        return toLoginResponse(customer);
    }

    private LoginResponse toLoginResponse(Customer customer) {
        return new LoginResponse(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
    }
}
