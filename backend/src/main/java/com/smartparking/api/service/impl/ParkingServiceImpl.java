package com.smartparking.api.service.impl;

import com.smartparking.api.model.*;
import com.smartparking.api.repository.*;
import com.smartparking.api.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingRecordRepository parkingRecordRepository;
    private final GlobalConfigurationRepository configRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Autowired
    public ParkingServiceImpl(
            VehicleRepository vehicleRepository,
            ParkingSpaceRepository parkingSpaceRepository,
            ParkingRecordRepository parkingRecordRepository,
            GlobalConfigurationRepository configRepository,
            ParkingLotRepository parkingLotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingRecordRepository = parkingRecordRepository;
        this.configRepository = configRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    @Override
    @Transactional
    public ParkingRecord handleVehicleEntry(String licensePlate, Integer parkingLotId) {
        // Check if vehicle exists, if not create a new one
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseGet(() -> {
                    Vehicle newVehicle = new Vehicle();
                    newVehicle.setLicensePlate(licensePlate);
                    return vehicleRepository.save(newVehicle);
                });

        // Check if vehicle is already parked
        Optional<ParkingRecord> activeRecord = parkingRecordRepository.findActiveRecordByLicensePlate(licensePlate);
        if (activeRecord.isPresent()) {
            throw new IllegalStateException("Vehicle with license plate " + licensePlate + " is already parked");
        }

        // Find an available parking space
        Optional<ParkingSpace> availableSpace = findAvailableSpace(parkingLotId);
        if (!availableSpace.isPresent()) {
            throw new IllegalStateException("No available parking spaces in the selected parking lot");
        }

        // Update parking space status
        ParkingSpace parkingSpace = availableSpace.get();
        parkingSpace.setStatus("OCCUPIED");
        parkingSpaceRepository.save(parkingSpace);

        // Create parking record
        ParkingRecord parkingRecord = new ParkingRecord();
        parkingRecord.setVehicle(vehicle);
        parkingRecord.setParkingSpace(parkingSpace);
        parkingRecord.setEntryTime(LocalDateTime.now());

        return parkingRecordRepository.save(parkingRecord);
    }

    @Override
    @Transactional
    public ParkingRecord handleVehicleExit(String licensePlate) {
        // Find active parking record for the vehicle
        Optional<ParkingRecord> activeRecordOpt = parkingRecordRepository.findActiveRecordByLicensePlate(licensePlate);
        if (!activeRecordOpt.isPresent()) {
            throw new IllegalStateException("No active parking record found for vehicle with license plate " + licensePlate);
        }

        ParkingRecord parkingRecord = activeRecordOpt.get();
        LocalDateTime exitTime = LocalDateTime.now();
        parkingRecord.setExitTime(exitTime);

        // Calculate fee
        BigDecimal fee = calculateParkingFee(parkingRecord.getEntryTime(), exitTime);
        parkingRecord.setFeePaid(fee);

        // Update parking space status
        ParkingSpace parkingSpace = parkingRecord.getParkingSpace();
        parkingSpace.setStatus("AVAILABLE");
        parkingSpaceRepository.save(parkingSpace);

        return parkingRecordRepository.save(parkingRecord);
    }

    @Override
    public BigDecimal calculateParkingFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        // Get hourly rate from configuration
        String hourlyRateStr = configRepository.findById("HOURLY_RATE")
                .map(GlobalConfiguration::getValue)
                .orElse("5.00"); // Default hourly rate if not configured
        
        BigDecimal hourlyRate = new BigDecimal(hourlyRateStr);
        
        // Calculate duration in hours (rounded up)
        Duration duration = Duration.between(entryTime, exitTime);
        long hours = duration.toHours();
        if (duration.toMinutes() % 60 > 0) {
            hours++; // Round up to the next hour
        }
        
        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }

    @Override
    public Optional<ParkingSpace> findAvailableSpace(Integer parkingLotId) {
        List<ParkingSpace> availableSpaces = parkingSpaceRepository.findByParkingLotIdAndStatus(parkingLotId, "AVAILABLE");
        return availableSpaces.stream().findFirst();
    }

    @Override
    public List<ParkingRecord> getActiveParkingRecords() {
        // Active records are those with null exitTime
        return parkingRecordRepository.findAll().stream()
                .filter(record -> record.getExitTime() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingRecord> getParkingRecordsByDateRange(LocalDateTime start, LocalDateTime end) {
        return parkingRecordRepository.findByEntryTimeBetween(start, end);
    }

    @Override
    public List<ParkingRecord> getParkingRecordsByVehicle(Integer vehicleId) {
        return parkingRecordRepository.findByVehicleId(vehicleId);
    }

    @Override
    public List<ParkingRecord> getParkingRecordsByParkingLotAndDateRange(Integer parkingLotId, LocalDateTime start, LocalDateTime end) {
        return parkingRecordRepository.findByParkingLotAndTimeRange(parkingLotId, start, end);
    }
    
    @Override
    public Integer getTotalAvailableSpaces() {
        // Count parking spaces that are not occupied
        return (int) parkingSpaceRepository.findAll().stream()
                .filter(space -> !space.isOccupied())
                .count();
    }
    
    @Override
    public Map<String, Integer> getOccupancyByDateRange(LocalDateTime start, LocalDateTime end) {
        // Get all parking records in the date range
        List<ParkingRecord> records = getParkingRecordsByDateRange(start, end);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Create a map to store occupancy counts per day
        Map<String, Integer> occupancyByDate = new HashMap<>();
        
        // Count vehicles that entered on each day
        records.forEach(record -> {
            String dateKey = record.getEntryTime().format(formatter);
            Integer currentCount = occupancyByDate.getOrDefault(dateKey, 0);
            occupancyByDate.put(dateKey, currentCount + 1);
        });
        
        return occupancyByDate;
    }
    
    @Override
    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAll();
    }
    
    @Override
    public Optional<ParkingLot> getParkingLotById(Integer id) {
        return parkingLotRepository.findById(id);
    }
    
    @Override
    @Transactional
    public ParkingLot createParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }
    
    @Override
    @Transactional
    public ParkingLot updateParkingLot(ParkingLot parkingLot) {
        // Make sure the parking lot exists
        if (!parkingLotRepository.existsById(parkingLot.getId())) {
            throw new IllegalArgumentException("Parking lot not found with id: " + parkingLot.getId());
        }
        return parkingLotRepository.save(parkingLot);
    }
    
    @Override
    @Transactional
    public void deleteParkingLot(Integer id) {
        // Make sure the parking lot exists
        if (!parkingLotRepository.existsById(id)) {
            throw new IllegalArgumentException("Parking lot not found with id: " + id);
        }
        
        // Delete all parking spaces in this lot first
        List<ParkingSpace> spaces = parkingSpaceRepository.findByParkingLotId(id);
        parkingSpaceRepository.deleteAll(spaces);
        
        // Then delete the parking lot
        parkingLotRepository.deleteById(id);
    }
    
    @Override
    public List<ParkingSpace> getParkingSpacesByLotId(Integer lotId) {
        return parkingSpaceRepository.findByParkingLotId(lotId);
    }
    
    @Override
    public List<ParkingSpace> getAvailableSpacesByLotId(Integer lotId) {
        return parkingSpaceRepository.findByParkingLotId(lotId).stream()
                .filter(space -> !space.isOccupied())
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ParkingSpace createParkingSpace(ParkingSpace parkingSpace) {
        // Make sure the associated parking lot exists
        if (!parkingLotRepository.existsById(parkingSpace.getParkingLot().getId())) {
            throw new IllegalArgumentException("Parking lot not found with id: " + parkingSpace.getParkingLot().getId());
        }
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    @Override
    @Transactional
    public ParkingSpace updateParkingSpace(ParkingSpace parkingSpace) {
        // Make sure the parking space exists
        if (!parkingSpaceRepository.existsById(parkingSpace.getId())) {
            throw new IllegalArgumentException("Parking space not found with id: " + parkingSpace.getId());
        }
        
        // Make sure the associated parking lot exists
        if (!parkingLotRepository.existsById(parkingSpace.getParkingLot().getId())) {
            throw new IllegalArgumentException("Parking lot not found with id: " + parkingSpace.getParkingLot().getId());
        }
        
        return parkingSpaceRepository.save(parkingSpace);
    }
    
    @Override
    @Transactional
    public void deleteParkingSpace(Integer spaceId) {
        // Make sure the parking space exists
        if (!parkingSpaceRepository.existsById(spaceId)) {
            throw new IllegalArgumentException("Parking space not found with id: " + spaceId);
        }
        
        // Check if the space is currently occupied
        ParkingSpace space = parkingSpaceRepository.findById(spaceId).orElseThrow();
        if (space.isOccupied()) {
            throw new IllegalStateException("Cannot delete an occupied parking space");
        }
        
        parkingSpaceRepository.deleteById(spaceId);
    }
} 