package com.smartparking.api.controller;

import com.smartparking.api.model.FinancialTransaction;
import com.smartparking.api.payload.ApiResponse;
import com.smartparking.api.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial")
public class FinancialController {

    @Autowired
    private FinancialService financialService;

    @PostMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<FinancialTransaction> createTransaction(
            @RequestParam Integer parkingRecordId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMethod) {
        FinancialTransaction transaction = financialService.createTransaction(parkingRecordId, amount, paymentMethod);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FinancialTransaction> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return financialService.getTransactionsByDateRange(start, end);
    }

    @GetMapping("/transactions/parking-lot/{parkingLotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FinancialTransaction> getTransactionsByParkingLot(
            @PathVariable Integer parkingLotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return financialService.getTransactionsByParkingLot(parkingLotId, start, end);
    }

    @GetMapping("/transactions/payment-method/{paymentMethod}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FinancialTransaction> getTransactionsByPaymentMethod(
            @PathVariable String paymentMethod) {
        return financialService.getTransactionsByPaymentMethod(paymentMethod);
    }

    @GetMapping("/transactions/parking-record/{parkingRecordId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<FinancialTransaction> getTransactionsByParkingRecord(
            @PathVariable Integer parkingRecordId) {
        return financialService.getTransactionsByParkingRecord(parkingRecordId);
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        BigDecimal revenue = financialService.getTotalRevenue(start, end);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, BigDecimal>> getDailyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Map<String, BigDecimal> dailyRevenue = financialService.getDailyRevenue(start, end);
        return ResponseEntity.ok(dailyRevenue);
    }

    @GetMapping("/revenue/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyRevenue(
            @RequestParam Integer year) {
        Map<String, BigDecimal> monthlyRevenue = financialService.getMonthlyRevenue(year);
        return ResponseEntity.ok(monthlyRevenue);
    }
} 