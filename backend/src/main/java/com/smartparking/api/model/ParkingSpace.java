package com.smartparking.api.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "parkingLot_id")
    private ParkingLot parkingLot;

    @Column(length = 10)
    private String spaceNumber;

    @Column(length = 20)
    private String status;

    @OneToMany(mappedBy = "parkingSpace")
    private Set<ParkingRecord> parkingRecords = new HashSet<>();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public String getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(String spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<ParkingRecord> getParkingRecords() {
        return parkingRecords;
    }

    public void setParkingRecords(Set<ParkingRecord> parkingRecords) {
        this.parkingRecords = parkingRecords;
    }

    public boolean isOccupied() {
        return "OCCUPIED".equals(status);
    }
} 