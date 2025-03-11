package com.smartparking.api.controller;

import com.smartparking.api.model.ParkingRecord;
import com.smartparking.api.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    public ResponseEntity<ParkingRecord> handleVehicleEntry(@RequestBody Map<String, Object> entryRequest) {
        String licensePlate = (String) entryRequest.get("licensePlate");
        Integer parkingLotId = (Integer) entryRequest.get("parkingLotId");

        try {
            ParkingRecord record = parkingService.handleVehicleEntry(licensePlate, parkingLotId);
            return ResponseEntity.ok(record);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/exit")
    public ResponseEntity<ParkingRecord> handleVehicleExit(@RequestBody Map<String, String> exitRequest) {
        String licensePlate = exitRequest.get("licensePlate");

        try {
            ParkingRecord record = parkingService.handleVehicleExit(licensePlate);
            return ResponseEntity.ok(record);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<ParkingRecord>> getActiveParkingRecords() {
        List<ParkingRecord> records = parkingService.getActiveParkingRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/records/date-range")
    public ResponseEntity<List<ParkingRecord>> getParkingRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<ParkingRecord> records = parkingService.getParkingRecordsByDateRange(start, end);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/records/vehicle/{vehicleId}")
    public ResponseEntity<List<ParkingRecord>> getParkingRecordsByVehicle(@PathVariable Integer vehicleId) {
        List<ParkingRecord> records = parkingService.getParkingRecordsByVehicle(vehicleId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/records/parking-lot/{parkingLotId}")
    public ResponseEntity<List<ParkingRecord>> getParkingRecordsByParkingLotAndDateRange(
            @PathVariable Integer parkingLotId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<ParkingRecord> records = parkingService.getParkingRecordsByParkingLotAndDateRange(parkingLotId, start, end);
        return ResponseEntity.ok(records);
    }

    @PostMapping("/plate-detection")
    public ResponseEntity<?> handlePlateDetection(@RequestBody Map<String, Object> detectionRequest) {
        String licensePlate = (String) detectionRequest.get("licensePlate");
        String action = (String) detectionRequest.get("action");
        Integer parkingLotId = (Integer) detectionRequest.get("parkingLotId");

        try {
            if ("ENTRY".equals(action)) {
                ParkingRecord record = parkingService.handleVehicleEntry(licensePlate, parkingLotId);
                return ResponseEntity.ok(record);
            } else if ("EXIT".equals(action)) {
                ParkingRecord record = parkingService.handleVehicleExit(licensePlate);
                return ResponseEntity.ok(record);
            } else {
                return ResponseEntity.badRequest().body("Invalid action: " + action);
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 