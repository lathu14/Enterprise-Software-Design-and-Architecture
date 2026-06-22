package com.hotel.paymentservice.repository;

import com.hotel.paymentservice.model.Payment;
import com.hotel.paymentservice.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Get all payments for a specific rental
    List<Payment> findByRentalId(Long rentalId);

    // Get all payments for a specific renter
    List<Payment> findByRenterId(Long renterId);

    // Get payment by transaction reference
    Optional<Payment> findByTransactionReference(String transactionReference);

    // Get payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // Check if a rental already has a successful payment
    boolean existsByRentalIdAndStatus(Long rentalId, PaymentStatus status);
}
