package org.example.movie_rental.repository;

import org.example.movie_rental.entity.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {

    @Query("SELECT DISTINCT o FROM RentalOrder o "
           + "LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.film "
           + "WHERE o.customer.customerId = :customerId ORDER BY o.orderDate DESC")
    List<RentalOrder> findByCustomerIdWithItems(@Param("customerId") Integer customerId);
}
