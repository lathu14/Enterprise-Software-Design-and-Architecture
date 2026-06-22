

package com.vehicleRental.vehicle_service.service.impl;

import com.vehicleRental.vehicle_service.model.Vehicle;
import com.vehicleRental.vehicle_service.repository.VehicleRepository;
import com.vehicleRental.vehicle_service.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class serviceImpl implements VehicleService {

    @Autowired
    private VehicleRepository repo;

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        return repo.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return repo.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {

        Vehicle v = repo.findById(id).orElseThrow();

        v.setVehicleName(vehicle.getVehicleName());
        v.setVehicleType(vehicle.getVehicleType());
        v.setSeatCount(vehicle.getSeatCount());
        v.setPricePerDay(vehicle.getPricePerDay());
        v.setLocation(vehicle.getLocation());
        v.setDriverAvailable(vehicle.isDriverAvailable());

        return repo.save(v);
    }

    @Override
    public void deleteVehicle(Long id) {
        repo.deleteById(id);
    }
}
