package com.hotel.paymentservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.hotel.paymentservice.dto.RentalDTO;
import com.hotel.paymentservice.dto.RefundRequest;
import com.hotel.paymentservice.exception.PaymentNotFoundException;
import com.hotel.paymentservice.model.Payment;
import com.hotel.paymentservice.model.PaymentStatus;
import com.hotel.paymentservice.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.rental-service.url}")
    private String rentalServiceUrl;

    @Value("${stripe.currency}")
    private String stripeCurrency;

    // ─── GET ALL PAYMENTS ─────────────────────────────────────────────────────
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // ─── GET PAYMENT BY ID ────────────────────────────────────────────────────
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    // ─── GET PAYMENTS BY BOOKING ──────────────────────────────────────────────
    public List<Payment> getPaymentsByRental(Long rentalId) {
        return paymentRepository.findByRentalId(rentalId);
    }

    // ─── GET PAYMENTS BY GUEST ────────────────────────────────────────────────
    public List<Payment> getPaymentsByRenter(Long renterId) {
        return paymentRepository.findByRenterId(renterId);
    }

    // ─── GET PAYMENTS BY STATUS ───────────────────────────────────────────────
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

   // ─── PROCESS PAYMENT (WITH STRIPE) ────────────────────────────────────────
public Payment processPayment(Payment payment) {

    // Try to verify rental — if Rental Service down, skip
    try {
        RentalDTO rental = restTemplate.getForObject(
                rentalServiceUrl + "/api/rentals/" + payment.getRentalId(),
                RentalDTO.class);

        if (rental != null) {
            if ("CANCELLED".equals(rental.getStatus())) {
                throw new IllegalArgumentException(
                        "Cannot process payment for a cancelled rental");
            }
            payment.setRenterId(rental.getRenterId());
            if (payment.getAmount() == null && rental.getTotalPrice() != null) {
                payment.setAmount(rental.getTotalPrice());
            }
        }
    } catch (ResourceAccessException e) {
        System.out.println(
            "⚠️ Rental Service unavailable — skipping rental verification");
    }

    // Prevent duplicate payments
    if (paymentRepository.existsByRentalIdAndStatus(
            payment.getRentalId(), PaymentStatus.SUCCESS)) {
        throw new IllegalArgumentException(
                "A successful payment already exists for rental ID: "
                        + payment.getRentalId());
    }

    // Set default amount if still null
    if (payment.getAmount() == null) {
        payment.setAmount(BigDecimal.ZERO);
    }

    // ─── Process Payment via Stripe ────────────────────────────────────────
    try {
        System.out.println("\n💳 Processing Stripe Payment...");
        System.out.println("   Rental ID: " + payment.getRentalId());
        System.out.println("   Amount: " + payment.getAmount());
        System.out.println("   Currency: " + stripeCurrency);

        long amountInCents = payment.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

        System.out.println("   Amount in cents: " + amountInCents);

        // Create PaymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(stripeCurrency)
                .setDescription("Vehicle Rental Payment - Rental ID: " + payment.getRentalId())
                .putMetadata("rentalId", String.valueOf(payment.getRentalId()))
                .putMetadata("renterId", String.valueOf(payment.getRenterId()))
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        // Set as SUCCESS since PaymentIntent created successfully
        payment.setTransactionReference(intent.getId());
        payment.setStatus(PaymentStatus.SUCCESS);

        // Print confirmed details (NOT original intent)
        System.out.println("✅ Stripe PaymentIntent confirmed: " + intent.getId());
        System.out.println("   Amount: LKR " + payment.getAmount());
        System.out.println("   Status: " + intent.getStatus());

    } catch (StripeException e) {
        System.err.println("\n❌ STRIPE ERROR OCCURRED:");
        System.err.println("   Error Type: " + e.getClass().getSimpleName());
        System.err.println("   Error Message: " + e.getMessage());
        System.err.println("   Stripe Status Code: " + e.getStatusCode());
        System.err.println("   Full Stack Trace:");
        e.printStackTrace();

        payment.setTransactionReference("FAILED-" + UUID.randomUUID()
                .toString().toUpperCase().substring(0, 8));
        payment.setStatus(PaymentStatus.FAILED);
    }

    return paymentRepository.save(payment);
}
    // ─── PROCESS REFUND ───────────────────────────────────────────────────────
    public Payment processRefund(Long paymentId, RefundRequest refundRequest) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // Can only refund a successful payment
        if (payment.getStatus() == PaymentStatus.FAILED ||
            payment.getStatus() == PaymentStatus.PENDING) {
            throw new IllegalArgumentException("Cannot refund a payment with status: " + payment.getStatus());
        }

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new IllegalArgumentException("Payment has already been fully refunded");
        }

        // Calculate how much is still refundable
        BigDecimal alreadyRefunded = payment.getRefundedAmount() != null
                ? payment.getRefundedAmount() : BigDecimal.ZERO;
        BigDecimal refundable = payment.getAmount().subtract(alreadyRefunded);

        if (refundRequest.getRefundAmount().compareTo(refundable) > 0) {
            throw new IllegalArgumentException(
                    "Refund amount exceeds refundable balance. Max refundable: " + refundable);
        }
        // After calculating refund amount, add this:
        try {
    // Create Stripe Refund
                RefundCreateParams refundParams = RefundCreateParams.builder()
            .setPaymentIntent(payment.getTransactionReference())
            .setAmount(refundRequest.getRefundAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .longValue())
            .build();

    com.stripe.model.Refund.create(refundParams);
    System.out.println("✅ Stripe refund processed successfully");

        } catch (StripeException e) {
    System.err.println("❌ Stripe refund error: " + e.getMessage());
            }

        // Apply refund
        BigDecimal newRefundedTotal = alreadyRefunded.add(refundRequest.getRefundAmount());
        payment.setRefundedAmount(newRefundedTotal);
        payment.setRefundReason(refundRequest.getReason());

        // If fully refunded, update status
        if (newRefundedTotal.compareTo(payment.getAmount()) == 0) {
            payment.setStatus(PaymentStatus.REFUNDED);
        } else {
            payment.setStatus(PaymentStatus.PARTIALLY_REFUNDED);
        }

        return paymentRepository.save(payment);
    }

    // ─── GET PAYMENT SUMMARY FOR A BOOKING ───────────────────────────────────
    public Map<String, Object> getPaymentSummary(Long rentalId) {
        List<Payment> payments = paymentRepository.findByRentalId(rentalId);

        BigDecimal totalPaid = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.SUCCESS
                          || p.getStatus() == PaymentStatus.REFUNDED
                          || p.getStatus() == PaymentStatus.PARTIALLY_REFUNDED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRefunded = payments.stream()
                .map(p -> p.getRefundedAmount() != null ? p.getRefundedAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("rentalId", rentalId);
        summary.put("totalPayments", payments.size());
        summary.put("totalPaid", totalPaid);
        summary.put("totalRefunded", totalRefunded);
        summary.put("netAmount", totalPaid.subtract(totalRefunded));
        summary.put("payments", payments);

        return summary;
    }
}
