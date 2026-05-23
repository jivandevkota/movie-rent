package org.example.movie_rental.repository;

import org.example.movie_rental.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT DISTINCT c FROM Cart c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.film WHERE c.customer.customerId = :customerId")
    Optional<Cart> findByCustomerIdWithItems(@Param("customerId") Integer customerId);

    Optional<Cart> findByCustomerCustomerId(Integer customerId);
}
