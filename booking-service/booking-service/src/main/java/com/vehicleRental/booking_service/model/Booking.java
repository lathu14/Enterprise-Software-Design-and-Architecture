package com.vehicleRental.booking_service.model;

import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long vehicleId;

    private String customerName;
    private String vehicleName;

    private String location;
    private String contactNumber;

    private String seatType;

    private boolean driverRequired;

    private String startDate;
    private String endDate;

    private String description;

    private double totalPrice;

    private String status;
}