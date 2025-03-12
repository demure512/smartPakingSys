package com.smartparking.api.controller;

import com.smartparking.api.model.ParkingLot;
import com.smartparking.api.model.ParkingSpace;
import com.smartparking.api.payload.ApiResponse;
import com.smartparking.api.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<ParkingLot> getAllParkingLots() {
        return parkingService.getAllParkingLots();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable Integer id) {
        return parkingService.getParkingLotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingLot> createParkingLot(@Valid @RequestBody ParkingLot parkingLot) {
        ParkingLot newParkingLot = parkingService.createParkingLot(parkingLot);
        return ResponseEntity.ok(newParkingLot);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingLot> updateParkingLot(
            @PathVariable Integer id,
            @Valid @RequestBody ParkingLot parkingLot) {
        parkingLot.setId(id);
        ParkingLot updatedParkingLot = parkingService.updateParkingLot(parkingLot);
        return ResponseEntity.ok(updatedParkingLot);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteParkingLot(@PathVariable Integer id) {
        parkingService.deleteParkingLot(id);
        return ResponseEntity.ok(new ApiResponse(true, "Parking lot deleted successfully"));
    }

    @GetMapping("/{id}/spaces")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<ParkingSpace> getParkingSpaces(@PathVariable Integer id) {
        return parkingService.getParkingSpacesByLotId(id);
    }

    @GetMapping("/{id}/available-spaces")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
    public List<ParkingSpace> getAvailableSpaces(@PathVariable Integer id) {
        return parkingService.getAvailableSpacesByLotId(id);
    }

    @PostMapping("/{id}/spaces")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> addParkingSpace(
            @PathVariable Integer id,
            @Valid @RequestBody ParkingSpace parkingSpace) {
        parkingSpace.setParkingLot(new ParkingLot());
        ParkingSpace newSpace = parkingService.createParkingSpace(parkingSpace);
        return ResponseEntity.ok(newSpace);
    }

    @PutMapping("/{lotId}/spaces/{spaceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> updateParkingSpace(
            @PathVariable Integer lotId,
            @PathVariable Integer spaceId,
            @Valid @RequestBody ParkingSpace parkingSpace) {
        parkingSpace.setId(spaceId);
        parkingSpace.setParkingLot(new ParkingLot());
        ParkingSpace updatedSpace = parkingService.updateParkingSpace(parkingSpace);
        return ResponseEntity.ok(updatedSpace);
    }

    @DeleteMapping("/{lotId}/spaces/{spaceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteParkingSpace(
            @PathVariable Integer lotId,
            @PathVariable Integer spaceId) {
        parkingService.deleteParkingSpace(spaceId);
        return ResponseEntity.ok(new ApiResponse(true, "Parking space deleted successfully"));
    }
} 