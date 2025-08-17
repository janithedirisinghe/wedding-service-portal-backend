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
    
    // Find meetings by customer
    List<Meeting> findByCustomer(Customer customer);
    
    // Find meetings by vendor
    List<Meeting> findByVendor(Vendor vendor);
    
    // Find meetings by customer and status
    List<Meeting> findByCustomerAndStatus(Customer customer, Meeting.MeetingStatus status);
    
    // Find meetings by vendor and status
    List<Meeting> findByVendorAndStatus(Vendor vendor, Meeting.MeetingStatus status);
    
    // Find meetings by status
    List<Meeting> findByStatus(Meeting.MeetingStatus status);
    
    // Find meetings by customer ID
    @Query("SELECT m FROM Meeting m WHERE m.customer.customerId = :customerId")
    List<Meeting> findByCustomerId(@Param("customerId") Long customerId);
    
    // Find meetings by vendor ID
    @Query("SELECT m FROM Meeting m WHERE m.vendor.venderId = :vendorId")
    List<Meeting> findByVendorId(@Param("vendorId") Long vendorId);
    
    // Find meetings by customer ID and status
    @Query("SELECT m FROM Meeting m WHERE m.customer.customerId = :customerId AND m.status = :status")
    List<Meeting> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") Meeting.MeetingStatus status);
    
    // Find meetings by vendor ID and status
    @Query("SELECT m FROM Meeting m WHERE m.vendor.venderId = :vendorId AND m.status = :status")
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
    
    // Find upcoming meetings for a vendor
    @Query("SELECT m FROM Meeting m WHERE m.vendor.venderId = :vendorId AND m.meetingDateTime > :currentDate AND m.status = 'CONFIRMED' ORDER BY m.meetingDateTime ASC")
    List<Meeting> findUpcomingMeetingsByVendorId(@Param("vendorId") Long vendorId, @Param("currentDate") LocalDateTime currentDate);
    
    // Find upcoming meetings for a customer
    @Query("SELECT m FROM Meeting m WHERE m.customer.customerId = :customerId AND m.meetingDateTime > :currentDate AND m.status = 'CONFIRMED' ORDER BY m.meetingDateTime ASC")
    List<Meeting> findUpcomingMeetingsByCustomerId(@Param("customerId") Long customerId, @Param("currentDate") LocalDateTime currentDate);
}
