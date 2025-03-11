package com.smartparking.api.repository;

import com.smartparking.api.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Integer> {
    List<ParkingRecord> findByVehicleId(Integer vehicleId);
    
    List<ParkingRecord> findByParkingSpaceId(Integer parkingSpaceId);
    
    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.vehicle.licensePlate = :licensePlate AND pr.exitTime IS NULL")
    Optional<ParkingRecord> findActiveRecordByLicensePlate(String licensePlate);
    
    List<ParkingRecord> findByEntryTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT pr FROM ParkingRecord pr JOIN pr.parkingSpace ps JOIN ps.parkingLot pl WHERE pl.id = :parkingLotId AND pr.entryTime BETWEEN :start AND :end")
    List<ParkingRecord> findByParkingLotAndTimeRange(Integer parkingLotId, LocalDateTime start, LocalDateTime end);
} 