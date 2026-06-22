package com.hotel.paymentservice.controller;

import com.hotel.paymentservice.dto.RefundRequest;
import com.hotel.paymentservice.model.Payment;
import com.hotel.paymentservice.model.PaymentStatus;
import com.hotel.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ✅ GET /api/payments — Retrieve all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // ✅ GET /api/payments/{id} — Retrieve payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // ✅ GET /api/payments/rental/{rentalId} — Get all payments for a rental
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<List<Payment>> getPaymentsByRental(@PathVariable Long rentalId) {
        return ResponseEntity.ok(paymentService.getPaymentsByRental(rentalId));
    }

    // ✅ GET /api/payments/renter/{renterId} — Get all payments for a renter
    @GetMapping("/renter/{renterId}")
    public ResponseEntity<List<Payment>> getPaymentsByRenter(@PathVariable Long renterId) {
        return ResponseEntity.ok(paymentService.getPaymentsByRenter(renterId));
    }

    // ✅ GET /api/payments/status/{status} — Get payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    // ✅ GET /api/payments/rental/{rentalId}/summary — Get payment summary for a rental
    @GetMapping("/rental/{rentalId}/summary")
    public ResponseEntity<Map<String, Object>> getPaymentSummary(@PathVariable Long rentalId) {
        return ResponseEntity.ok(paymentService.getPaymentSummary(rentalId));
    }

    // ✅ POST /api/payments — Process a new payment
    @PostMapping
    public ResponseEntity<Payment> processPayment(@Valid @RequestBody Payment payment) {
        Payment processed = paymentService.processPayment(payment);
        return new ResponseEntity<>(processed, HttpStatus.CREATED);
    }

    // ✅ POST /api/payments/{id}/refund — Process a refund
    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> processRefund(@PathVariable Long id,
                                                  @Valid @RequestBody RefundRequest refundRequest) {
        return ResponseEntity.ok(paymentService.processRefund(id, refundRequest));
    }
}
