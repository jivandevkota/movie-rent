package org.example.movie_rental.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final EntityManager em;

    @GetMapping("/revenue-monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue() {
        List<?> rows = em.createQuery(
                "SELECT FUNCTION('DATE_FORMAT', p.paymentDate, '%Y-%m') as month, SUM(p.amount) " +
                "FROM Payment p GROUP BY month ORDER BY month")
                .getResultList();
        List<Map<String, Object>> data = rows.stream()
                .map(row -> {
                    Object[] cols = (Object[]) row;
                    return Map.<String, Object>of("month", cols[0], "revenue", String.format("%.2f", cols[1]));
                })
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-films")
    public ResponseEntity<List<Map<String, Object>>> getTopFilms() {
        List<?> rows = em.createQuery(
                "SELECT f.title, COUNT(r) FROM Rental r JOIN r.inventory i JOIN i.film f " +
                "GROUP BY f.filmId, f.title ORDER BY COUNT(r) DESC")
                .setMaxResults(10)
                .getResultList();
        List<Map<String, Object>> data = rows.stream()
                .map(row -> {
                    Object[] cols = (Object[]) row;
                    return Map.<String, Object>of("filmTitle", cols[0], "rentalCount", cols[1]);
                })
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/category-performance")
    public ResponseEntity<List<Map<String, Object>>> getCategoryPerformance() {
        List<?> rows = em.createQuery(
                "SELECT c.name, SUM(p.amount) FROM Payment p " +
                "JOIN p.rental r JOIN r.inventory i JOIN i.film f " +
                "JOIN f.filmCategories fc JOIN fc.category c " +
                "GROUP BY c.name ORDER BY SUM(p.amount) DESC")
                .setMaxResults(10)
                .getResultList();
        List<Map<String, Object>> data = rows.stream()
                .map(row -> {
                    Object[] cols = (Object[]) row;
                    return Map.<String, Object>of("category", cols[0], "revenue", String.format("%.2f", cols[1]));
                })
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {
        List<?> rows = em.createQuery(
                "SELECT c.customerId, c.firstName, c.lastName, c.email, COUNT(r) as rentalCount " +
                "FROM Customer c JOIN c.rentals r " +
                "GROUP BY c.customerId ORDER BY rentalCount DESC")
                .setMaxResults(10)
                .getResultList();
        List<Map<String, Object>> data = rows.stream()
                .map(row -> {
                    Object[] cols = (Object[]) row;
                    return Map.<String, Object>of(
                            "customerId", cols[0],
                            "firstName", cols[1],
                            "lastName", cols[2],
                            "email", cols[3],
                            "rentalCount", cols[4]);
                })
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/revenue-by-rating")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByRating() {
        List<?> rows = em.createQuery(
                "SELECT f.rating, SUM(p.amount) FROM Payment p " +
                "JOIN p.rental r JOIN r.inventory i JOIN i.film f " +
                "WHERE f.rating IS NOT NULL " +
                "GROUP BY f.rating ORDER BY SUM(p.amount) DESC")
                .getResultList();
        List<Map<String, Object>> data = rows.stream()
                .map(row -> {
                    Object[] cols = (Object[]) row;
                    return Map.<String, Object>of("rating", cols[0], "revenue", String.format("%.2f", cols[1]));
                })
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/rentals-by-month")
    public ResponseEntity<List<Map<String, Object>>> getRentalsByMonth() {
        List<?> rows = em.createQuery(
                "SELECT FUNCTION('DATE_FORMAT', r.rentalDate, '%Y-%m') as month, COUNT(r) " +
                "FROM Rental r GROUP BY month ORDER BY month")
                .getResultList();
        List<Map<String, Object>> data = rows.stream()
                .map(row -> {
                    Object[] cols = (Object[]) row;
                    return Map.<String, Object>of("month", cols[0], "count", cols[1]);
                })
                .toList();
        return ResponseEntity.ok(data);
    }
}
