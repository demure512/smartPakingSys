package com.smartparking.api.service.impl;

import com.smartparking.api.model.FinancialTransaction;
import com.smartparking.api.model.ParkingRecord;
import com.smartparking.api.repository.FinancialTransactionRepository;
import com.smartparking.api.repository.ParkingRecordRepository;
import com.smartparking.api.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.YearMonth;
import java.time.Month;
import java.util.stream.Collectors;

@Service
public class FinancialServiceImpl implements FinancialService {

    private final FinancialTransactionRepository transactionRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    @Autowired
    public FinancialServiceImpl(
            FinancialTransactionRepository transactionRepository,
            ParkingRecordRepository parkingRecordRepository) {
        this.transactionRepository = transactionRepository;
        this.parkingRecordRepository = parkingRecordRepository;
    }

    @Override
    @Transactional
    public FinancialTransaction createTransaction(Integer parkingRecordId, BigDecimal amount, String paymentMethod) {
        ParkingRecord parkingRecord = parkingRecordRepository.findById(parkingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Parking record not found with ID: " + parkingRecordId));

        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setParkingRecord(parkingRecord);
        transaction.setAmount(amount);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setTransactionTime(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<FinancialTransaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionTimeBetween(start, end);
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.sumAmountByDateRange(start, end);
    }

    @Override
    public List<FinancialTransaction> getTransactionsByParkingLot(Integer parkingLotId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByParkingLotAndTimeRange(parkingLotId, start, end);
    }

    @Override
    public List<FinancialTransaction> getTransactionsByPaymentMethod(String paymentMethod) {
        return transactionRepository.findAll().stream()
                .filter(transaction -> paymentMethod.equals(transaction.getPaymentMethod()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FinancialTransaction> getTransactionsByParkingRecord(Integer parkingRecordId) {
        return transactionRepository.findByParkingRecordId(parkingRecordId);
    }
    
    @Override
    public Map<String, BigDecimal> getRevenueByDateRange(LocalDateTime start, LocalDateTime end) {
        List<FinancialTransaction> transactions = getTransactionsByDateRange(start, end);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        Map<String, BigDecimal> revenueByDate = new HashMap<>();
        
        // Group transactions by date and sum the amounts
        transactions.forEach(transaction -> {
            String dateKey = transaction.getTransactionTime().format(formatter);
            BigDecimal currentAmount = revenueByDate.getOrDefault(dateKey, BigDecimal.ZERO);
            revenueByDate.put(dateKey, currentAmount.add(transaction.getAmount()));
        });
        
        return revenueByDate;
    }
    
    @Override
    public Map<String, BigDecimal> getDailyRevenue(LocalDateTime start, LocalDateTime end) {
        // This method is similar to getRevenueByDateRange but can be customized 
        // to provide additional formatting or processing specific to daily reports
        List<FinancialTransaction> transactions = getTransactionsByDateRange(start, end);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        Map<String, BigDecimal> dailyRevenue = new HashMap<>();
        
        transactions.forEach(transaction -> {
            String dateKey = transaction.getTransactionTime().format(formatter);
            BigDecimal currentAmount = dailyRevenue.getOrDefault(dateKey, BigDecimal.ZERO);
            dailyRevenue.put(dateKey, currentAmount.add(transaction.getAmount()));
        });
        
        return dailyRevenue;
    }
    
    @Override
    public Map<String, BigDecimal> getMonthlyRevenue(Integer year) {
        // Get start and end dates for the specified year
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        
        // Get all transactions for the year
        List<FinancialTransaction> yearlyTransactions = getTransactionsByDateRange(startOfYear, endOfYear);
        
        // Initialize map with all months (even those with zero revenue)
        Map<String, BigDecimal> monthlyRevenue = new HashMap<>();
        for (Month month : Month.values()) {
            monthlyRevenue.put(month.toString(), BigDecimal.ZERO);
        }
        
        // Aggregate revenues by month
        yearlyTransactions.forEach(transaction -> {
            String monthKey = transaction.getTransactionTime().getMonth().toString();
            BigDecimal currentAmount = monthlyRevenue.get(monthKey);
            monthlyRevenue.put(monthKey, currentAmount.add(transaction.getAmount()));
        });
        
        return monthlyRevenue;
    }
} 