package com.smartparking.api.service;

import com.smartparking.api.model.FinancialTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface FinancialService {
    /**
     * Create a new financial transaction
     * @param parkingRecordId The ID of the associated parking record
     * @param amount The transaction amount
     * @param paymentMethod The payment method used
     * @return The created financial transaction
     */
    FinancialTransaction createTransaction(Integer parkingRecordId, BigDecimal amount, String paymentMethod);
    
    /**
     * Get transactions by date range
     * @param start Start date
     * @param end End date
     * @return List of transactions in the date range
     */
    List<FinancialTransaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Get total revenue for a date range
     * @param start Start date
     * @param end End date
     * @return Total revenue
     */
    BigDecimal getTotalRevenue(LocalDateTime start, LocalDateTime end);
    
    /**
     * Get transactions by parking lot and date range
     * @param parkingLotId The ID of the parking lot
     * @param start Start date
     * @param end End date
     * @return List of transactions for the parking lot in the date range
     */
    List<FinancialTransaction> getTransactionsByParkingLot(Integer parkingLotId, LocalDateTime start, LocalDateTime end);
    
    /**
     * Get transactions by payment method
     * @param paymentMethod The payment method
     * @return List of transactions with the specified payment method
     */
    List<FinancialTransaction> getTransactionsByPaymentMethod(String paymentMethod);
    
    /**
     * Get transactions for a specific parking record
     * @param parkingRecordId The ID of the parking record
     * @return List of transactions for the parking record
     */
    List<FinancialTransaction> getTransactionsByParkingRecord(Integer parkingRecordId);
    
    /**
     * Get revenue data by date range, formatted for chart visualization
     * @param start Start date
     * @param end End date
     * @return Map of date strings to revenue amounts
     */
    Map<String, BigDecimal> getRevenueByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Get daily revenue data for a date range
     * @param start Start date
     * @param end End date
     * @return Map of date strings to daily revenue amounts
     */
    Map<String, BigDecimal> getDailyRevenue(LocalDateTime start, LocalDateTime end);
    
    /**
     * Get monthly revenue data for a specific year
     * @param year The year to get monthly revenue for
     * @return Map of month strings to monthly revenue amounts
     */
    Map<String, BigDecimal> getMonthlyRevenue(Integer year);
} 