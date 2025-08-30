package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Meeting;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    
    // Find meetings by customer with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.customer = :customer")
    List<Meeting> findByCustomer(@Param("customer") Customer customer);
    
    // Find meetings by vendor with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.vendor = :vendor")
    List<Meeting> findByVendor(@Param("vendor") Vendor vendor);
    
    // Find meetings by customer and status with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.customer = :customer AND m.status = :status")
    List<Meeting> findByCustomerAndStatus(@Param("customer") Customer customer, @Param("status") Meeting.MeetingStatus status);
    
    // Find meetings by vendor and status with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.vendor = :vendor AND m.status = :status")
    List<Meeting> findByVendorAndStatus(@Param("vendor") Vendor vendor, @Param("status") Meeting.MeetingStatus status);
    
    // Find meetings by status
    List<Meeting> findByStatus(Meeting.MeetingStatus status);
    
    // Find meetings by customer ID with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.customer.customerId = :customerId")
    List<Meeting> findByCustomerId(@Param("customerId") Long customerId);
    
    // Find meetings by vendor ID with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.vendor.venderId = :vendorId")
    List<Meeting> findByVendorId(@Param("vendorId") Long vendorId);
    
    // Find meetings by customer ID and status with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.customer.customerId = :customerId AND m.status = :status")
    List<Meeting> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") Meeting.MeetingStatus status);
    
    // Find meetings by vendor ID and status with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.vendor.venderId = :vendorId AND m.status = :status")
    List<Meeting> findByVendorIdAndStatus(@Param("vendorId") Long vendorId, @Param("status") Meeting.MeetingStatus status);
    
    // Find meetings within a date range
    @Query("SELECT m FROM Meeting m WHERE m.meetingDateTime BETWEEN :startDate AND :endDate")
    List<Meeting> findMeetingsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find meetings by vendor and date range
    @Query("SELECT m FROM Meeting m WHERE m.vendor.venderId = :vendorId AND m.meetingDateTime BETWEEN :startDate AND :endDate")
    List<Meeting> findByVendorIdAndDateRange(@Param("vendorId") Long vendorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find meetings by customer and date range
    @Query("SELECT m FROM Meeting m WHERE m.customer.customerId = :customerId AND m.meetingDateTime BETWEEN :startDate AND :endDate")
    List<Meeting> findByCustomerIdAndDateRange(@Param("customerId") Long customerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find upcoming meetings for a vendor with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.vendor.venderId = :vendorId AND m.meetingDateTime > :currentDate AND m.status = 'CONFIRMED' ORDER BY m.meetingDateTime ASC")
    List<Meeting> findUpcomingMeetingsByVendorId(@Param("vendorId") Long vendorId, @Param("currentDate") LocalDateTime currentDate);
    
    // Find upcoming meetings for a customer with eager loading
    @Query("SELECT m FROM Meeting m JOIN FETCH m.customer c JOIN FETCH c.user JOIN FETCH m.vendor v JOIN FETCH v.user WHERE m.customer.customerId = :customerId AND m.meetingDateTime > :currentDate AND m.status = 'CONFIRMED' ORDER BY m.meetingDateTime ASC")
    List<Meeting> findUpcomingMeetingsByCustomerId(@Param("customerId") Long customerId, @Param("currentDate") LocalDateTime currentDate);
    
    // Count meetings by vendor ID
    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.vendor.venderId = :vendorId")
    Long countByVendorId(@Param("vendorId") Long vendorId);
    
    // Count completed meetings by vendor ID
    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.vendor.venderId = :vendorId AND m.status = 'COMPLETED'")
    Long countCompletedMeetingsByVendorId(@Param("vendorId") Long vendorId);
    
    // Count meetings by customer ID
    Long countByCustomer_CustomerId(Long customerId);
}
