package org.example.movie_rental.service;

import java.util.List;
import java.util.Map;

public interface EnhancedDashboardService {
    List<Map<String, Object>> getRecentRentals(int limit);
    List<Map<String, Object>> getTopActors(int limit);
    List<Map<String, Object>> getLowInventoryFilms(int threshold);
    List<Map<String, Object>> getMonthlyRevenue();
    Map<String, Object> getActorStats(Integer actorId);
}
