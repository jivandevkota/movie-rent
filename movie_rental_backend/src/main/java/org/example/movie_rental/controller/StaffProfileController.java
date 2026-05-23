package org.example.movie_rental.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffProfileController {

    private final EntityManager em;

    @GetMapping("/{staffId}/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Integer staffId) {
        Query q = em.createQuery(
                "SELECT s.staffId, s.firstName, s.lastName, s.email, s.active, s.username " +
                "FROM Staff s WHERE s.staffId = :id");
        q.setParameter("id", staffId);
        Object[] row = (Object[]) q.getSingleResult();

        Long rentalCount = (Long) em.createQuery(
                "SELECT COUNT(r) FROM Rental r WHERE r.staff.staffId = :id")
                .setParameter("id", staffId).getSingleResult();

        java.math.BigDecimal totalRevenue = (java.math.BigDecimal) em.createQuery(
                "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.staff.staffId = :id")
                .setParameter("id", staffId).getSingleResult();

        Long activeRentals = (Long) em.createQuery(
                "SELECT COUNT(r) FROM Rental r WHERE r.staff.staffId = :id AND r.returnDate IS NULL")
                .setParameter("id", staffId).getSingleResult();

        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("staffId", row[0]);
        profile.put("firstName", row[1]);
        profile.put("lastName", row[2]);
        profile.put("email", row[3]);
        profile.put("active", row[4]);
        profile.put("username", row[5]);
        profile.put("totalRentalsProcessed", rentalCount);
        profile.put("totalRevenueProcessed", totalRevenue);
        profile.put("activeRentalsManaged", activeRentals);
        return ResponseEntity.ok(profile);
    }
}
