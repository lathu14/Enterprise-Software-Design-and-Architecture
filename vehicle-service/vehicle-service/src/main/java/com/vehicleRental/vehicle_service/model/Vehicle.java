package com.vehicleRental.vehicle_service.model;



import jakarta.persistence.*;
        import lombok.*;

@Entity
@Table(name="vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleName;
    private String vehicleType;
    private int seatCount;
    private double pricePerDay;
    private String location;
    private boolean driverAvailable;
    private boolean availability;
    private String description;
    private String imageUrl;
}