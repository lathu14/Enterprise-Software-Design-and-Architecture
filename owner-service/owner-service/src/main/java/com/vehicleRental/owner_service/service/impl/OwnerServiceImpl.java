package com.vehicleRental.owner_service.service.impl;

import com.vehicleRental.owner_service.model.Owner;
import com.vehicleRental.owner_service.repository.OwnerRepository;
import com.vehicleRental.owner_service.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository repository;

    @Override
    public Owner saveOwner(Owner owner) {
        return repository.save(owner);
    }

    @Override
    public List<Owner> getAllOwners() {
        return repository.findAll();
    }

    @Override
    public Owner getOwnerById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Owner updateOwner(Long id, Owner owner) {

        Owner existing = repository.findById(id).orElseThrow();

        existing.setOwnerName(owner.getOwnerName());
        existing.setEmail(owner.getEmail());
        existing.setPassword(owner.getPassword());
        existing.setContactNumber(owner.getContactNumber());

        return repository.save(existing);
    }

    @Override
    public void deleteOwner(Long id) {
        repository.deleteById(id);
    }
}