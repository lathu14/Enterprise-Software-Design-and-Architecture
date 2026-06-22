package com.vehicleRental.user_service.service.impl;

import com.vehicleRental.user_service.model.User;
import com.vehicleRental.user_service.repository.UserRepository;
import com.vehicleRental.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public User registerUser(User user) {
        return repo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public User updateUser(Long id, User user) {

        User u = repo.findById(id).orElseThrow();

        u.setFullName(user.getFullName());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        u.setPhoneNumber(user.getPhoneNumber());
        u.setRole(user.getRole());

        return repo.save(u);
    }

    @Override
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}