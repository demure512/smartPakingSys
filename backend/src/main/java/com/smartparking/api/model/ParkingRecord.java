package com.smartparking.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parking_records")
public class ParkingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "parkingSpace_id")
    private ParkingSpace parkingSpace;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal feePaid;

    @OneToMany(mappedBy = "parkingRecord")
    private Set<FinancialTransaction> financialTransactions = new HashSet<>();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public BigDecimal getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(BigDecimal feePaid) {
        this.feePaid = feePaid;
    }

    public Set<FinancialTransaction> getFinancialTransactions() {
        return financialTransactions;
    }

    public void setFinancialTransactions(Set<FinancialTransaction> financialTransactions) {
        this.financialTransactions = financialTransactions;
    }
} 