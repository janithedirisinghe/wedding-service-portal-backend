package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Payment;
import com.example.WeddingVenderMngSystem.entity.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Find payment by booking ID
    Optional<Payment> findByBooking_BookingId(Long bookingId);
    
    // Find payment by Stripe payment intent ID
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    
    // Find payments by customer ID
    @Query("SELECT p FROM Payment p WHERE p.booking.customer.customerId = :customerId")
    List<Payment> findByCustomerId(@Param("customerId") Long customerId);
    
    // Find payments by vendor ID
    @Query("SELECT p FROM Payment p WHERE p.booking.service.vendor.venderId = :vendorId")
    List<Payment> findByVendorId(@Param("vendorId") Long vendorId);
    
    // Find payments by status
    List<Payment> findByStatus(PaymentStatus status);
    
    // Find payments by customer ID and status
    @Query("SELECT p FROM Payment p WHERE p.booking.customer.customerId = :customerId AND p.status = :status")
    List<Payment> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") PaymentStatus status);
    
    // Find payment by booking ID and customer ID (for security)
    @Query("SELECT p FROM Payment p WHERE p.booking.bookingId = :bookingId AND p.booking.customer.customerId = :customerId")
    Optional<Payment> findByBookingIdAndCustomerId(@Param("bookingId") Long bookingId, @Param("customerId") Long customerId);
}
