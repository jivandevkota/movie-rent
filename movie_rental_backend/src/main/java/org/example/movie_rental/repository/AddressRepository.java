package org.example.movie_rental.repository;

import org.example.movie_rental.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}