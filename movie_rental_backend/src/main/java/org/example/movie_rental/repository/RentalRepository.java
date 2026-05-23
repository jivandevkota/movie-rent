package org.example.movie_rental.repository;

import org.example.movie_rental.dto.CustomerRentalDto;
import org.example.movie_rental.entity.Inventory;
import org.example.movie_rental.entity.Rental;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

    @Query("SELECT new org.example.movie_rental.dto.CustomerRentalDto(" +
           "  r.rentalId, r.inventory.film.filmId, r.inventory.film.title, " +
           "  CONCAT('/img/films/', r.inventory.film.filmId, '.jpg'), " +
           "  r.rentalDate, r.returnDate) " +
           "FROM Rental r WHERE r.customer.customerId = :customerId " +
           "ORDER BY r.rentalDate DESC")
    List<CustomerRentalDto> findRentalsByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT i FROM Inventory i WHERE i.film.filmId = :filmId " +
           "AND i.inventoryId NOT IN (" +
           "  SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL" +
           ")")
    List<Inventory> findAvailableInventory(@Param("filmId") Integer filmId, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.film.filmId = :filmId " +
           "AND i.inventoryId NOT IN (" +
           "  SELECT r.inventory.inventoryId FROM Rental r WHERE r.returnDate IS NULL" +
           ")")
    long countAvailableInventory(@Param("filmId") Integer filmId);

    @Query("SELECT COUNT(r) FROM Rental r JOIN r.inventory i WHERE i.film.filmId = :filmId")
    long countByFilmId(@Param("filmId") Integer filmId);

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.returnDate IS NULL")
    long countActiveRentals();

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.returnDate IS NULL " +
           "AND FUNCTION('DATEDIFF', CURRENT_DATE, r.rentalDate) > r.inventory.film.rentalDuration")
    long countOverdueRentals();

    @Query(value = """
            SELECT r.rentalId, c.customerId, c.firstName, c.lastName,
                   f.filmId, f.title, r.rentalDate, r.returnDate, f.rentalDuration
            FROM Rental r
            JOIN r.customer c
            JOIN r.inventory i
            JOIN i.film f
            ORDER BY r.rentalDate DESC, r.rentalId DESC
            """,
            countQuery = "SELECT COUNT(r) FROM Rental r JOIN r.customer c JOIN r.inventory i JOIN i.film f")
    org.springframework.data.domain.Page<Object[]> findAllRentalData(org.springframework.data.domain.Pageable pageable);

    @Query(value = """
            SELECT r.rentalId, c.customerId, c.firstName, c.lastName,
                   f.filmId, f.title, r.rentalDate, r.returnDate, f.rentalDuration
            FROM Rental r
            JOIN r.customer c
            JOIN r.inventory i
            JOIN i.film f
            WHERE r.returnDate IS NULL
            ORDER BY r.rentalDate DESC, r.rentalId DESC
            """,
            countQuery = "SELECT COUNT(r) FROM Rental r JOIN r.customer c JOIN r.inventory i JOIN i.film f WHERE r.returnDate IS NULL")
    org.springframework.data.domain.Page<Object[]> findActiveRentals(org.springframework.data.domain.Pageable pageable);

    @Query(value = """
            SELECT r.rentalId, c.customerId, c.firstName, c.lastName,
                   f.filmId, f.title, r.rentalDate, r.returnDate, f.rentalDuration
            FROM Rental r
            JOIN r.customer c
            JOIN r.inventory i
            JOIN i.film f
            WHERE r.returnDate IS NOT NULL
            ORDER BY r.rentalDate DESC, r.rentalId DESC
            """,
            countQuery = "SELECT COUNT(r) FROM Rental r JOIN r.customer c JOIN r.inventory i JOIN i.film f WHERE r.returnDate IS NOT NULL")
    org.springframework.data.domain.Page<Object[]> findReturnedRentals(org.springframework.data.domain.Pageable pageable);

    @Query(value = """
            SELECT r.rentalId, c.customerId, c.firstName, c.lastName,
                   f.filmId, f.title, r.rentalDate, r.returnDate, f.rentalDuration
            FROM Rental r
            JOIN r.customer c
            JOIN r.inventory i
            JOIN i.film f
            WHERE r.returnDate IS NULL
            AND FUNCTION('DATEDIFF', CURRENT_DATE, r.rentalDate) > f.rentalDuration
            ORDER BY r.rentalDate DESC, r.rentalId DESC
            """,
            countQuery = "SELECT COUNT(r) FROM Rental r JOIN r.customer c JOIN r.inventory i JOIN i.film f WHERE r.returnDate IS NULL AND FUNCTION('DATEDIFF', CURRENT_DATE, r.rentalDate) > f.rentalDuration")
    org.springframework.data.domain.Page<Object[]> findOverdueRentals(org.springframework.data.domain.Pageable pageable);
}
