package com.smartparking.api.repository;

import com.smartparking.api.model.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Integer> {
    List<FinancialTransaction> findByParkingRecordId(Integer parkingRecordId);
    
    List<FinancialTransaction> findByTransactionTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(ft.amount) FROM FinancialTransaction ft WHERE ft.transactionTime BETWEEN :start AND :end")
    BigDecimal sumAmountByDateRange(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT ft FROM FinancialTransaction ft JOIN ft.parkingRecord pr JOIN pr.parkingSpace ps JOIN ps.parkingLot pl WHERE pl.id = :parkingLotId AND ft.transactionTime BETWEEN :start AND :end")
    List<FinancialTransaction> findByParkingLotAndTimeRange(Integer parkingLotId, LocalDateTime start, LocalDateTime end);
} 