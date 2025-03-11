package com.smartparking.api.repository;

import com.smartparking.api.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Integer> {
    List<ParkingLot> findByNameContaining(String name);
} 