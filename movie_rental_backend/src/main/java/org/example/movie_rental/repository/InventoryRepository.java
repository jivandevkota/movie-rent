package org.example.movie_rental.repository;

import org.example.movie_rental.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByFilmFilmId(Integer filmId);
    long countByFilmFilmId(Integer filmId);
}
