package com.smartparking.api.service.impl;

import com.smartparking.api.model.FinancialTransaction;
import com.smartparking.api.model.ParkingLot;
import com.smartparking.api.model.ParkingRecord;
import com.smartparking.api.model.Vehicle;
import com.smartparking.api.repository.FinancialTransactionRepository;
import com.smartparking.api.repository.ParkingLotRepository;
import com.smartparking.api.repository.ParkingRecordRepository;
import com.smartparking.api.repository.VehicleRepository;
import com.smartparking.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ReportServiceImpl(
            ParkingRecordRepository parkingRecordRepository,
            FinancialTransactionRepository financialTransactionRepository,
            ParkingLotRepository parkingLotRepository,
            VehicleRepository vehicleRepository) {
        this.parkingRecordRepository = parkingRecordRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public byte[] generateOccupancyReport(LocalDateTime start, LocalDateTime end, Integer parkingLotId, String format) {
        // Get parking records for the date range
        List<ParkingRecord> records;
        if (parkingLotId != null) {
            records = parkingRecordRepository.findByParkingLotAndTimeRange(parkingLotId, start, end);
        } else {
            records = parkingRecordRepository.findByEntryTimeBetween(start, end);
        }

        // Process records to get occupancy data
        Map<String, Integer> occupancyByDate = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        records.forEach(record -> {
            String dateKey = record.getEntryTime().format(formatter);
            occupancyByDate.merge(dateKey, 1, Integer::sum);
        });

        // Convert the data to the requested format and return as byte array
        return generateReportBytes(occupancyByDate, "Occupancy Report", format);
    }

    @Override
    public byte[] generateRevenueReport(LocalDateTime start, LocalDateTime end, String paymentMethod, String format) {
        // Get financial transactions for the date range
        List<FinancialTransaction> transactions;
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            transactions = financialTransactionRepository.findByTransactionTimeBetween(start, end).stream()
                    .filter(t -> paymentMethod.equals(t.getPaymentMethod()))
                    .collect(Collectors.toList());
        } else {
            transactions = financialTransactionRepository.findByTransactionTimeBetween(start, end);
        }

        // Process transactions to get revenue data
        Map<String, BigDecimal> revenueByDate = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        transactions.forEach(transaction -> {
            String dateKey = transaction.getTransactionTime().format(formatter);
            BigDecimal currentAmount = revenueByDate.getOrDefault(dateKey, BigDecimal.ZERO);
            revenueByDate.put(dateKey, currentAmount.add(transaction.getAmount()));
        });

        // Convert the data to the requested format and return as byte array
        return generateReportBytes(revenueByDate, "Revenue Report", format);
    }

    @Override
    public byte[] generateVehicleUsageReport(LocalDateTime start, LocalDateTime end, String vehicleType, String format) {
        // Get parking records for the date range
        List<ParkingRecord> records = parkingRecordRepository.findByEntryTimeBetween(start, end);
        
        // Filter by vehicle type if specified
        if (vehicleType != null && !vehicleType.isEmpty()) {
            records = records.stream()
                    .filter(record -> {
                        Vehicle vehicle = record.getVehicle();
                        return vehicle != null && vehicleType.equals(vehicle.getType());
                    })
                    .collect(Collectors.toList());
        }

        // Process records to get vehicle usage data
        Map<String, Integer> usageByVehicleType = new HashMap<>();
        
        records.forEach(record -> {
            Vehicle vehicle = record.getVehicle();
            if (vehicle != null) {
                String type = vehicle.getType() != null ? vehicle.getType() : "Unknown";
                usageByVehicleType.merge(type, 1, Integer::sum);
            }
        });

        // Convert the data to the requested format and return as byte array
        return generateReportBytes(usageByVehicleType, "Vehicle Usage Report", format);
    }

    @Override
    public byte[] generatePeakHoursReport(LocalDateTime start, LocalDateTime end, Integer parkingLotId, String format) {
        // Get parking records for the date range
        List<ParkingRecord> records;
        if (parkingLotId != null) {
            records = parkingRecordRepository.findByParkingLotAndTimeRange(parkingLotId, start, end);
        } else {
            records = parkingRecordRepository.findByEntryTimeBetween(start, end);
        }

        // Process records to get peak hours data
        Map<String, Integer> occupancyByHour = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
        
        records.forEach(record -> {
            String hourKey = record.getEntryTime().format(formatter);
            occupancyByHour.merge(hourKey, 1, Integer::sum);
        });

        // Convert the data to the requested format and return as byte array
        return generateReportBytes(occupancyByHour, "Peak Hours Report", format);
    }

    @Override
    public byte[] generateMonthlySummaryReport(Integer year, Integer month, String format) {
        // Create date range for the entire month
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime end = LocalDateTime.of(year, month, yearMonth.lengthOfMonth(), 23, 59, 59);

        // Get parking records and financial transactions for the month
        List<ParkingRecord> records = parkingRecordRepository.findByEntryTimeBetween(start, end);
        List<FinancialTransaction> transactions = financialTransactionRepository.findByTransactionTimeBetween(start, end);

        // Process data to create summary
        int totalParkingCount = records.size();
        
        BigDecimal totalRevenue = transactions.stream()
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<Integer, Long> parkingCountByLot = records.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getParkingSpace().getParkingLot().getId(),
                        Collectors.counting()));
        
        // Create summary map
        Map<String, Object> summary = new HashMap<>();
        summary.put("year", year);
        summary.put("month", month);
        summary.put("totalParkingCount", totalParkingCount);
        summary.put("totalRevenue", totalRevenue);
        summary.put("parkingCountByLot", parkingCountByLot);

        // Convert the data to the requested format and return as byte array
        return generateReportBytes(summary, "Monthly Summary Report", format);
    }

    /**
     * Helper method to convert report data to bytes in the requested format
     * @param data The report data
     * @param reportName The name of the report
     * @param format The requested format (PDF, EXCEL, CSV)
     * @return The report as byte array
     */
    private byte[] generateReportBytes(Object data, String reportName, String format) {
        // In a real implementation, this would use libraries like Apache POI for Excel, 
        // iText for PDF, or Apache Commons CSV for CSV files to generate actual reports.
        // For this implementation, we'll just return a placeholder byte array.
        String placeholder = "Report: " + reportName + ", Format: " + format + ", Data: " + data.toString();
        return placeholder.getBytes();
    }
} 