package com.smartparking.api.service.impl;

import com.smartparking.api.model.Vehicle;
import com.smartparking.api.repository.VehicleRepository;
import com.smartparking.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> getVehicleById(Integer id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Optional<Vehicle> getVehicleByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        // Ensure the vehicle doesn't already exist
        if (vehicleRepository.findByLicensePlate(vehicle.getLicensePlate()).isPresent()) {
            throw new IllegalArgumentException("Vehicle with license plate " + vehicle.getLicensePlate() + " already exists");
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle updateVehicle(Vehicle vehicle) {
        // Ensure the vehicle exists
        if (!vehicleRepository.existsById(vehicle.getId())) {
            throw new IllegalArgumentException("Vehicle with ID " + vehicle.getId() + " not found");
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    public void deleteVehicle(Integer id) {
        // Ensure the vehicle exists
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehicle with ID " + id + " not found");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<Vehicle> searchVehicles(String licensePlate, String make, String model) {
        return vehicleRepository.findAll().stream()
            .filter(vehicle -> 
                (licensePlate == null || vehicle.getLicensePlate().toLowerCase().contains(licensePlate.toLowerCase())) &&
                (make == null || vehicle.getMake().toLowerCase().contains(make.toLowerCase())) &&
                (model == null || vehicle.getModel().toLowerCase().contains(model.toLowerCase()))
            )
            .collect(Collectors.toList());
    }
} 