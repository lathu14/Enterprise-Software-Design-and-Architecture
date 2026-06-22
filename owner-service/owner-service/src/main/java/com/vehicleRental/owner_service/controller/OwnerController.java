package com.vehicleRental.owner_service.controller;

import com.vehicleRental.owner_service.model.Owner;
import com.vehicleRental.owner_service.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin("*")
public class OwnerController {

    @Autowired
    private OwnerService service;

    @PostMapping
    public Owner saveOwner(@RequestBody Owner owner){
        return service.saveOwner(owner);
    }

    @GetMapping
    public List<Owner> getAllOwners(){
        return service.getAllOwners();
    }

    @GetMapping("/{id}")
    public Owner getOwnerById(@PathVariable Long id){
        return service.getOwnerById(id);
    }

    @PutMapping("/{id}")
    public Owner updateOwner(@PathVariable Long id,
                             @RequestBody Owner owner){
        return service.updateOwner(id,owner);
    }

    @DeleteMapping("/{id}")
    public String deleteOwner(@PathVariable Long id){

        service.deleteOwner(id);

        return "Owner Deleted Successfully";
    }
}