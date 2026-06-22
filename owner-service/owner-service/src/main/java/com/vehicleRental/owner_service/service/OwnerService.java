package com.vehicleRental.owner_service.service;

import com.vehicleRental.owner_service.model.Owner;
import java.util.List;

public interface OwnerService {

    Owner saveOwner(Owner owner);

    List<Owner> getAllOwners();

    Owner getOwnerById(Long id);

    Owner updateOwner(Long id, Owner owner);

    void deleteOwner(Long id);
}