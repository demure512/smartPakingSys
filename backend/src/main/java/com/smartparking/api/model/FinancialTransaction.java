package com.smartparking.api.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "parkingRecord_id")
    private ParkingRecord parkingRecord;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 50)
    private String paymentMethod;

    private LocalDateTime transactionTime;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ParkingRecord getParkingRecord() {
        return parkingRecord;
    }

    public void setParkingRecord(ParkingRecord parkingRecord) {
        this.parkingRecord = parkingRecord;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }
} 