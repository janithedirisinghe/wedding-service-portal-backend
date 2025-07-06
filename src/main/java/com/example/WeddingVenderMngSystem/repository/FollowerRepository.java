package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Follower;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    
    // Find all active followers for a specific vendor
    @Query("SELECT f FROM Follower f WHERE f.vendor.venderId = :vendorId AND f.isActive = true")
    List<Follower> findActiveFollowersByVendorId(@Param("vendorId") Long vendorId);
    
    // Find all vendors that a customer is following
    @Query("SELECT f FROM Follower f WHERE f.customer.customerId = :customerId AND f.isActive = true")
    List<Follower> findActiveFollowingsByCustomerId(@Param("customerId") Long customerId);
    
    // Check if a customer is following a specific vendor
    @Query("SELECT f FROM Follower f WHERE f.customer.customerId = :customerId AND f.vendor.venderId = :vendorId AND f.isActive = true")
    Optional<Follower> findActiveFollowerByCustomerAndVendor(@Param("customerId") Long customerId, @Param("vendorId") Long vendorId);
    
    // Find follower relationship (active or inactive)
    Optional<Follower> findByCustomerAndVendor(Customer customer, Vendor vendor);
    
    // Count active followers for a vendor
    @Query("SELECT COUNT(f) FROM Follower f WHERE f.vendor.venderId = :vendorId AND f.isActive = true")
    Long countActiveFollowersByVendorId(@Param("vendorId") Long vendorId);
    
    // Count vendors a customer is following
    @Query("SELECT COUNT(f) FROM Follower f WHERE f.customer.customerId = :customerId AND f.isActive = true")
    Long countActiveFollowingsByCustomerId(@Param("customerId") Long customerId);
    
    // Find all customers following a vendor
    @Query("SELECT f.customer FROM Follower f WHERE f.vendor.venderId = :vendorId AND f.isActive = true")
    List<Customer> findCustomersFollowingVendor(@Param("vendorId") Long vendorId);
    
    // Find all vendors followed by a customer
    @Query("SELECT f.vendor FROM Follower f WHERE f.customer.customerId = :customerId AND f.isActive = true")
    List<Vendor> findVendorsFollowedByCustomer(@Param("customerId") Long customerId);
    
    // Check if customer is following vendor (boolean result)
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Follower f WHERE f.customer.customerId = :customerId AND f.vendor.venderId = :vendorId AND f.isActive = true")
    boolean isCustomerFollowingVendor(@Param("customerId") Long customerId, @Param("vendorId") Long vendorId);
}
