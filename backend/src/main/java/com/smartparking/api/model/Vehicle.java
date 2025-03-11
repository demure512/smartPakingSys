package com.smartparking.api.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.smartparking.api.model.Customer;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String licensePlate;

    @Column(length = 50)
    private String make;

    @Column(length = 50)
    private String model;

    @Column(length = 30)
    private String type;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "vehicle")
    private Set<ParkingRecord> parkingRecords = new HashSet<>();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<ParkingRecord> getParkingRecords() {
        return parkingRecords;
    }

    public void setParkingRecords(Set<ParkingRecord> parkingRecords) {
        this.parkingRecords = parkingRecords;
    }
} 