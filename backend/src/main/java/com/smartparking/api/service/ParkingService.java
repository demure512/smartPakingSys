package com.smartparking.api.service;

import com.smartparking.api.model.ParkingRecord;
import com.smartparking.api.model.ParkingSpace;
import com.smartparking.api.model.ParkingLot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ParkingService {
    /**
     * Handle vehicle entry
     * @param licensePlate The license plate of the entering vehicle
     * @param parkingLotId The ID of the parking lot
     * @return The created parking record
     */
    ParkingRecord handleVehicleEntry(String licensePlate, Integer parkingLotId);
    
    /**
     * Handle vehicle exit
     * @param licensePlate The license plate of the exiting vehicle
     * @return The updated parking record with exit time and fee
     */
    ParkingRecord handleVehicleExit(String licensePlate);
    
    /**
     * Calculate parking fee
     * @param entryTime Entry time
     * @param exitTime Exit time
     * @return The calculated fee
     */
    BigDecimal calculateParkingFee(LocalDateTime entryTime, LocalDateTime exitTime);
    
    /**
     * Find an available parking space in a parking lot
     * @param parkingLotId The ID of the parking lot
     * @return An available parking space, if any
     */
    Optional<ParkingSpace> findAvailableSpace(Integer parkingLotId);
    
    /**
     * Get active parking records (vehicles currently parked)
     * @return List of active parking records
     */
    List<ParkingRecord> getActiveParkingRecords();
    
    /**
     * Get parking records by date range
     * @param start Start date
     * @param end End date
     * @return List of parking records in the date range
     */
    List<ParkingRecord> getParkingRecordsByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Get parking records for a specific vehicle
     * @param vehicleId The ID of the vehicle
     * @return List of parking records for the vehicle
     */
    List<ParkingRecord> getParkingRecordsByVehicle(Integer vehicleId);
    
    /**
     * Get parking records for a specific parking lot in a date range
     * @param parkingLotId The ID of the parking lot
     * @param start Start date
     * @param end End date
     * @return List of parking records for the parking lot in the date range
     */
    List<ParkingRecord> getParkingRecordsByParkingLotAndDateRange(Integer parkingLotId, LocalDateTime start, LocalDateTime end);

    /**
     * Get the total number of available parking spaces across all parking lots
     * @return The total available spaces
     */
    Integer getTotalAvailableSpaces();
    
    /**
     * Get occupancy data by date range for visualization in charts
     * @param start Start date
     * @param end End date
     * @return Map of date strings to occupancy counts
     */
    Map<String, Integer> getOccupancyByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Get all parking lots
     * @return List of all parking lots
     */
    List<ParkingLot> getAllParkingLots();
    
    /**
     * Get a specific parking lot by ID
     * @param id The ID of the parking lot
     * @return The parking lot if found
     */
    Optional<ParkingLot> getParkingLotById(Integer id);
    
    /**
     * Create a new parking lot
     * @param parkingLot The parking lot to create
     * @return The created parking lot
     */
    ParkingLot createParkingLot(ParkingLot parkingLot);
    
    /**
     * Update an existing parking lot
     * @param parkingLot The updated parking lot data
     * @return The updated parking lot
     */
    ParkingLot updateParkingLot(ParkingLot parkingLot);
    
    /**
     * Delete a parking lot
     * @param id The ID of the parking lot to delete
     */
    void deleteParkingLot(Integer id);
    
    /**
     * Get all parking spaces for a specific parking lot
     * @param lotId The ID of the parking lot
     * @return List of parking spaces in the lot
     */
    List<ParkingSpace> getParkingSpacesByLotId(Integer lotId);
    
    /**
     * Get available parking spaces for a specific parking lot
     * @param lotId The ID of the parking lot
     * @return List of available parking spaces in the lot
     */
    List<ParkingSpace> getAvailableSpacesByLotId(Integer lotId);
    
    /**
     * Create a new parking space
     * @param parkingSpace The parking space to create
     * @return The created parking space
     */
    ParkingSpace createParkingSpace(ParkingSpace parkingSpace);
    
    /**
     * Update an existing parking space
     * @param parkingSpace The updated parking space data
     * @return The updated parking space
     */
    ParkingSpace updateParkingSpace(ParkingSpace parkingSpace);
    
    /**
     * Delete a parking space
     * @param spaceId The ID of the parking space to delete
     */
    void deleteParkingSpace(Integer spaceId);
} 