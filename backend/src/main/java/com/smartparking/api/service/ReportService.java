package com.smartparking.api.service;

import java.time.LocalDateTime;

public interface ReportService {
    /**
     * Generate an occupancy report for a date range and optional parking lot
     * @param start Start date
     * @param end End date
     * @param parkingLotId Optional parking lot ID (null for all lots)
     * @param format Report format (PDF, EXCEL, CSV)
     * @return The report as byte array
     */
    byte[] generateOccupancyReport(LocalDateTime start, LocalDateTime end, Integer parkingLotId, String format);
    
    /**
     * Generate a revenue report for a date range and optional payment method
     * @param start Start date
     * @param end End date
     * @param paymentMethod Optional payment method (null for all methods)
     * @param format Report format (PDF, EXCEL, CSV)
     * @return The report as byte array
     */
    byte[] generateRevenueReport(LocalDateTime start, LocalDateTime end, String paymentMethod, String format);
    
    /**
     * Generate a vehicle usage report for a date range and optional vehicle type
     * @param start Start date
     * @param end End date
     * @param vehicleType Optional vehicle type (null for all types)
     * @param format Report format (PDF, EXCEL, CSV)
     * @return The report as byte array
     */
    byte[] generateVehicleUsageReport(LocalDateTime start, LocalDateTime end, String vehicleType, String format);
    
    /**
     * Generate a peak hours report for a date range and optional parking lot
     * @param start Start date
     * @param end End date
     * @param parkingLotId Optional parking lot ID (null for all lots)
     * @param format Report format (PDF, EXCEL, CSV)
     * @return The report as byte array
     */
    byte[] generatePeakHoursReport(LocalDateTime start, LocalDateTime end, Integer parkingLotId, String format);
    
    /**
     * Generate a monthly summary report for a specific year and month
     * @param year The year
     * @param month The month (1-12)
     * @param format Report format (PDF, EXCEL, CSV)
     * @return The report as byte array
     */
    byte[] generateMonthlySummaryReport(Integer year, Integer month, String format);
} 