package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Booking;
import com.example.WeddingVenderMngSystem.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find bookings by customer ID
    List<Booking> findByCustomer_CustomerId(Long customerId);
    
    // Find bookings by service vendor ID
    @Query("SELECT b FROM Booking b WHERE b.service.vendor.venderId = :vendorId")
    List<Booking> findByVendorId(@Param("vendorId") Long vendorId);
    
    // Find bookings by customer ID and status
    List<Booking> findByCustomer_CustomerIdAndStatus(Long customerId, BookingStatus status);
    
    // Find bookings by vendor ID and status
    @Query("SELECT b FROM Booking b WHERE b.service.vendor.venderId = :vendorId AND b.status = :status")
    List<Booking> findByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") BookingStatus status);
    
    // Find booking by ID and customer ID (for security)
    Optional<Booking> findByBookingIdAndCustomer_CustomerId(Long bookingId, Long customerId);
    
    // Find booking by ID and vendor ID (for security)
    @Query("SELECT b FROM Booking b WHERE b.bookingId = :bookingId AND b.service.vendor.venderId = :vendorId")
    Optional<Booking> findByBookingIdAndVendorId(@Param("bookingId") Long bookingId, @Param("vendorId") Long vendorId);
    
    // Find pending bookings for a vendor
    @Query("SELECT b FROM Booking b WHERE b.service.vendor.venderId = :vendorId AND b.status = 'PENDING'")
    List<Booking> findPendingBookingsByVendorId(@Param("vendorId") Long vendorId);
    
    // Analytics methods
    Long countByStatus(BookingStatus status);
    
    // Count bookings by vendor ID
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.service.vendor.venderId = :vendorId")
    Long countByVendorId(@Param("vendorId") Long vendorId);
    
    // Count completed bookings by vendor ID
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.service.vendor.venderId = :vendorId AND b.status = 'COMPLETED'")
    Long countCompletedBookingsByVendorId(@Param("vendorId") Long vendorId);
    
    // Count bookings by customer ID
    Long countByCustomer_CustomerId(Long customerId);
}
