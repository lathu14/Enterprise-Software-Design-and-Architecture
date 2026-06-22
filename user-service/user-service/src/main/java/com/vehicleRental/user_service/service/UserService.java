package com.vehicleRental.user_service.service;

import com.vehicleRental.user_service.model.User;

import java.util.List;

public interface UserService {

    User registerUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}