package com.smartparking.api.controller;

import com.smartparking.api.model.Vehicle;
import com.smartparking.api.model.ParkingRecord;
import com.smartparking.api.payload.ApiResponse;
import com.smartparking.api.service.ParkingService;
import com.smartparking.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Integer id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/plate/{licensePlate}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<Vehicle> getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return vehicleService.getVehicleByLicensePlate(licensePlate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle newVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.ok(newVehicle);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Integer id,
            @Valid @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicle);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(new ApiResponse(true, "Vehicle deleted successfully"));
    }

    @GetMapping("/{id}/parking-records")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<ParkingRecord> getVehicleParkingRecords(@PathVariable Integer id) {
        return parkingService.getParkingRecordsByVehicle(id);
    }

    @PostMapping("/entry")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<ParkingRecord> handleVehicleEntry(
            @RequestParam String licensePlate,
            @RequestParam Integer parkingLotId) {
        ParkingRecord record = parkingService.handleVehicleEntry(licensePlate, parkingLotId);
        return ResponseEntity.ok(record);
    }

    @PostMapping("/exit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<ParkingRecord> handleVehicleExit(
            @RequestParam String licensePlate) {
        ParkingRecord record = parkingService.handleVehicleExit(licensePlate);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<Vehicle> searchVehicles(
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model) {
        return vehicleService.searchVehicles(licensePlate, make, model);
    }
} 