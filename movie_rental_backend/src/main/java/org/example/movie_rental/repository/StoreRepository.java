package org.example.movie_rental.repository;

import org.example.movie_rental.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}