package com.vehicleRental.vehicle_service.repository;




import com.vehicleRental.vehicle_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}