package com.vehicleRental.user_service.controller;

import com.vehicleRental.user_service.model.User;
import com.vehicleRental.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public User registerUser(@RequestBody User user){
        return service.registerUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return service.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody User user){
        return service.updateUser(id,user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){

        service.deleteUser(id);

        return "User Deleted";
    }
}