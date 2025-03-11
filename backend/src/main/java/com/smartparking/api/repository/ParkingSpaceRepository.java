package com.smartparking.api.repository;

import com.smartparking.api.model.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Integer> {
    List<ParkingSpace> findByParkingLotId(Integer parkingLotId);
    List<ParkingSpace> findByParkingLotIdAndStatus(Integer parkingLotId, String status);
    Optional<ParkingSpace> findByParkingLotIdAndSpaceNumber(Integer parkingLotId, String spaceNumber);
} 