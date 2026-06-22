package com.vehicleRental.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String phoneNumber;

    private String role;
}