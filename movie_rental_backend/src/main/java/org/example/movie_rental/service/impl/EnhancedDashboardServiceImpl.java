package org.example.movie_rental.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.example.movie_rental.service.EnhancedDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EnhancedDashboardServiceImpl implements EnhancedDashboardService {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentRentals(int limit) {
        Query query = entityManager.createQuery(
                "SELECT r.rentalId, f.title, c.firstName, c.lastName, r.rentalDate, r.returnDate " +
                "FROM Rental r JOIN r.inventory i JOIN i.film f JOIN r.customer c " +
                "ORDER BY r.rentalDate DESC");
        query.setMaxResults(limit);
        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("rentalId", row[0]);
            map.put("filmTitle", row[1]);
            map.put("customerFirstName", row[2]);
            map.put("customerLastName", row[3]);
            map.put("rentalDate", row[4]);
            map.put("returnDate", row[5]);
            list.add(map);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTopActors(int limit) {
        Query query = entityManager.createQuery(
                "SELECT a.actorId, CONCAT(a.firstName, ' ', a.lastName) as name, COUNT(r) as rentalCount " +
                "FROM Actor a JOIN a.filmActors fa JOIN fa.film f JOIN f.inventories i JOIN i.rentals r " +
                "GROUP BY a.actorId ORDER BY rentalCount DESC");
        query.setMaxResults(limit);
        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("actorId", row[0]);
            map.put("name", row[1]);
            map.put("rentalCount", row[2]);
            list.add(map);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLowInventoryFilms(int threshold) {
        Query query = entityManager.createQuery(
                "SELECT f.filmId, f.title, COUNT(i) " +
                "FROM Film f LEFT JOIN f.inventories i " +
                "GROUP BY f.filmId HAVING COUNT(i) < :threshold ORDER BY COUNT(i)");
        query.setParameter("threshold", (long) threshold);
        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("filmId", row[0]);
            map.put("title", row[1]);
            map.put("copies", row[2]);
            list.add(map);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMonthlyRevenue() {
        Query query = entityManager.createNativeQuery(
                "SELECT DATE_FORMAT(p.payment_date, '%Y-%m') as month, COALESCE(SUM(p.amount), 0) as revenue " +
                "FROM payment p GROUP BY month ORDER BY month DESC");
        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("month", row[0]);
            map.put("revenue", row[1]);
            list.add(map);
        }
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getActorStats(Integer actorId) {
        // Get actor details
        Query actorQuery = entityManager.createQuery(
                "SELECT a.actorId, a.firstName, a.lastName FROM Actor a WHERE a.actorId = :id");
        actorQuery.setParameter("id", actorId);
        Object[] actorRow = (Object[]) actorQuery.getSingleResult();

        // Film count
        Query filmCountQuery = entityManager.createQuery(
                "SELECT COUNT(fa) FROM FilmActor fa WHERE fa.actor.actorId = :id");
        filmCountQuery.setParameter("id", actorId);
        Long filmCount = (Long) filmCountQuery.getSingleResult();

        // Total rentals across all films
        Query totalRentalsQuery = entityManager.createQuery(
                "SELECT COUNT(r) FROM Rental r JOIN r.inventory i JOIN i.film f JOIN f.filmActors fa WHERE fa.actor.actorId = :id");
        totalRentalsQuery.setParameter("id", actorId);
        Long totalRentals = (Long) totalRentalsQuery.getSingleResult();

        // Top 5 most rented films
        Query topFilmsQuery = entityManager.createQuery(
                "SELECT f.filmId, f.title, COUNT(r) as rentalCount " +
                "FROM Film f JOIN f.filmActors fa JOIN f.inventories i JOIN i.rentals r " +
                "WHERE fa.actor.actorId = :id " +
                "GROUP BY f.filmId ORDER BY rentalCount DESC");
        topFilmsQuery.setParameter("id", actorId);
        topFilmsQuery.setMaxResults(5);
        List<Object[]> topFilmsRows = topFilmsQuery.getResultList();
        List<Map<String, Object>> topFilms = new ArrayList<>();
        for (Object[] row : topFilmsRows) {
            Map<String, Object> filmMap = new LinkedHashMap<>();
            filmMap.put("filmId", row[0]);
            filmMap.put("title", row[1]);
            filmMap.put("rentalCount", row[2]);
            topFilms.add(filmMap);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("actorId", actorRow[0]);
        result.put("firstName", actorRow[1]);
        result.put("lastName", actorRow[2]);
        result.put("name", actorRow[1] + " " + actorRow[2]);
        result.put("filmCount", filmCount);
        result.put("totalRentals", totalRentals);
        result.put("topFilms", topFilms);
        return result;
    }
}
