package com.smartparking.api.service;

import com.smartparking.api.model.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleService {
    /**
     * Get all vehicles
     * @return List of all vehicles
     */
    List<Vehicle> getAllVehicles();

    /**
     * Get a vehicle by ID
     * @param id The vehicle ID
     * @return Optional containing the vehicle if found
     */
    Optional<Vehicle> getVehicleById(Integer id);

    /**
     * Get a vehicle by license plate
     * @param licensePlate The license plate number
     * @return Optional containing the vehicle if found
     */
    Optional<Vehicle> getVehicleByLicensePlate(String licensePlate);

    /**
     * Create a new vehicle
     * @param vehicle The vehicle to create
     * @return The created vehicle
     */
    Vehicle createVehicle(Vehicle vehicle);

    /**
     * Update an existing vehicle
     * @param vehicle The vehicle to update
     * @return The updated vehicle
     */
    Vehicle updateVehicle(Vehicle vehicle);

    /**
     * Delete a vehicle
     * @param id The ID of the vehicle to delete
     */
    void deleteVehicle(Integer id);

    /**
     * Search vehicles by criteria
     * @param licensePlate Optional license plate to search for
     * @param make Optional make to search for
     * @param model Optional model to search for
     * @return List of matching vehicles
     */
    List<Vehicle> searchVehicles(String licensePlate, String make, String model);
} 