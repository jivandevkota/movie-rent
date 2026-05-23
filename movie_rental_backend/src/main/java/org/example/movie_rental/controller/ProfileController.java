package org.example.movie_rental.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customers/{customerId}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final EntityManager em;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Integer customerId) {
        Query q = em.createQuery(
                "SELECT c.customerId, c.firstName, c.lastName, c.email, c.active, c.createDate " +
                "FROM Customer c WHERE c.customerId = :id");
        q.setParameter("id", customerId);
        Object[] row = (Object[]) q.getSingleResult();

        Long rentalCount = (Long) em.createQuery(
                "SELECT COUNT(r) FROM Rental r WHERE r.customer.customerId = :id")
                .setParameter("id", customerId).getSingleResult();

        java.math.BigDecimal totalSpent = (java.math.BigDecimal) em.createQuery(
                "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.customer.customerId = :id")
                .setParameter("id", customerId).getSingleResult();

        Long activeRentals = (Long) em.createQuery(
                "SELECT COUNT(r) FROM Rental r WHERE r.customer.customerId = :id AND r.returnDate IS NULL")
                .setParameter("id", customerId).getSingleResult();

        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("customerId", row[0]);
        profile.put("firstName", row[1]);
        profile.put("lastName", row[2]);
        profile.put("email", row[3]);
        profile.put("active", row[4]);
        profile.put("createDate", row[5]);
        profile.put("totalRentals", rentalCount);
        profile.put("totalSpent", totalSpent);
        profile.put("activeRentals", activeRentals);
        return ResponseEntity.ok(profile);
    }
}
