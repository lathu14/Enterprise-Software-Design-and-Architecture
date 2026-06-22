package com.vehicleRental.owner_service.repository;

import com.vehicleRental.owner_service.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner,Long> {
}