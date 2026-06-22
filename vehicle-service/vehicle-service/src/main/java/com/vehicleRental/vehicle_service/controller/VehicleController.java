package com.vehicleRental.vehicle_service.controller;

import com.vehicleRental.vehicle_service.model.Vehicle;
import com.vehicleRental.vehicle_service.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return service.addVehicle(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return service.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return service.getVehicleById(id);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id,
                                 @RequestBody Vehicle vehicle) {

        return service.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public String deleteVehicle(@PathVariable Long id) {

        service.deleteVehicle(id);

        return "Vehicle Deleted";
    }
}