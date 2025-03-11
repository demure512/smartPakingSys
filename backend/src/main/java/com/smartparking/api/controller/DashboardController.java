package com.smartparking.api.controller;

import com.smartparking.api.model.ParkingRecord;
import com.smartparking.api.model.SystemLog;
import com.smartparking.api.service.ParkingService;
import com.smartparking.api.service.FinancialService;
import com.smartparking.api.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private FinancialService financialService;

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<?> getDashboardStats() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);

        // Get statistics
        List<ParkingRecord> activeRecords = parkingService.getActiveParkingRecords();
        BigDecimal todayRevenue = financialService.getTotalRevenue(today, tomorrow);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVehicles", activeRecords.size());
        stats.put("availableSpaces", parkingService.getTotalAvailableSpaces());
        stats.put("todayRevenue", todayRevenue);
        stats.put("activeParking", activeRecords.size());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent-activity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<?> getRecentActivity() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<SystemLog> recentLogs = systemLogService.getLogsByDateRange(oneHourAgo, LocalDateTime.now());
        return ResponseEntity.ok(recentLogs);
    }

    @GetMapping("/revenue-chart")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getRevenueChart(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, BigDecimal> revenueData = financialService.getRevenueByDateRange(start, end);
        return ResponseEntity.ok(revenueData);
    }

    @GetMapping("/occupancy-chart")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<?> getOccupancyChart(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Integer> occupancyData = parkingService.getOccupancyByDateRange(start, end);
        return ResponseEntity.ok(occupancyData);
    }
} 