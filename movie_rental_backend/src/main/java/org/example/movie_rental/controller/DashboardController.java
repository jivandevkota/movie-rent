package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.DashboardStatsDto;
import org.example.movie_rental.service.DashboardService;
import org.example.movie_rental.service.EnhancedDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final EnhancedDashboardService enhancedDashboardService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/dashboard/recent-rentals")
    public ResponseEntity<List<Map<String, Object>>> getRecentRentals(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(enhancedDashboardService.getRecentRentals(limit));
    }

    @GetMapping("/dashboard/top-actors")
    public ResponseEntity<List<Map<String, Object>>> getTopActors(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(enhancedDashboardService.getTopActors(limit));
    }

    @GetMapping("/dashboard/low-inventory")
    public ResponseEntity<List<Map<String, Object>>> getLowInventoryFilms(
            @RequestParam(defaultValue = "3") int threshold) {
        return ResponseEntity.ok(enhancedDashboardService.getLowInventoryFilms(threshold));
    }

    @GetMapping("/dashboard/monthly-revenue")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue() {
        return ResponseEntity.ok(enhancedDashboardService.getMonthlyRevenue());
    }
}
