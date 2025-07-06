package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Follower;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.FollowerRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FollowerService {
    
    @Autowired
    private FollowerRepository followerRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    /**
     * Follow a vendor by a customer (using userId)
     */
    public Follower followVendor(Long userId, Long vendorId) {
        // Convert userId to customerId
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user id: " + userId));
        
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
        
        // Check if already following
        Optional<Follower> existingFollower = followerRepository.findByCustomerAndVendor(customer, vendor);
        
        if (existingFollower.isPresent()) {
            Follower follower = existingFollower.get();
            if (follower.getIsActive()) {
                throw new RuntimeException("Customer is already following this vendor");
            } else {
                // Reactivate the follow relationship
                follower.setIsActive(true);
                follower.setFollowedAt(LocalDateTime.now());
                return followerRepository.save(follower);
            }
        } else {
            // Create new follow relationship
            Follower newFollower = new Follower(customer, vendor);
            return followerRepository.save(newFollower);
        }
    }
    
    /**
     * Unfollow a vendor by a customer (using userId)
     */
    public void unfollowVendor(Long userId, Long vendorId) {
        // Convert userId to customerId
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user id: " + userId));
        
        Optional<Follower> follower = followerRepository.findActiveFollowerByCustomerAndVendor(customer.getCustomerId(), vendorId);
        
        if (follower.isPresent()) {
            Follower existingFollower = follower.get();
            existingFollower.setIsActive(false);
            followerRepository.save(existingFollower);
        } else {
            throw new RuntimeException("Follow relationship not found");
        }
    }
    
    /**
     * Get all customers following a specific vendor
     */
    public List<Customer> getVendorFollowers(Long vendorId) {
        return followerRepository.findCustomersFollowingVendor(vendorId);
    }
    
    /**
     * Get all vendors followed by a specific customer (using userId)
     */
    public List<Vendor> getCustomerFollowing(Long userId) {
        // Convert userId to customerId
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user id: " + userId));
        
        return followerRepository.findVendorsFollowedByCustomer(customer.getCustomerId());
    }
    
    /**
     * Check if a customer is following a vendor (using userId)
     */
    public boolean isFollowing(Long userId, Long vendorId) {
        // Convert userId to customerId
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElse(null);
        
        if (customer == null) {
            return false;
        }
        
        return followerRepository.isCustomerFollowingVendor(customer.getCustomerId(), vendorId);
    }
    
    /**
     * Get follower count for a vendor
     */
    public Long getFollowerCount(Long vendorId) {
        return followerRepository.countActiveFollowersByVendorId(vendorId);
    }
    
    /**
     * Get following count for a customer (using userId)
     */
    public Long getFollowingCount(Long userId) {
        // Convert userId to customerId
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElse(null);
        
        if (customer == null) {
            return 0L;
        }
        
        return followerRepository.countActiveFollowingsByCustomerId(customer.getCustomerId());
    }
    
    /**
     * Get all active followers for a vendor
     */
    public List<Follower> getActiveFollowers(Long vendorId) {
        return followerRepository.findActiveFollowersByVendorId(vendorId);
    }
    
    /**
     * Get all active following relationships for a customer (using userId)
     */
    public List<Follower> getActiveFollowing(Long userId) {
        // Convert userId to customerId
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user id: " + userId));
        
        return followerRepository.findActiveFollowingsByCustomerId(customer.getCustomerId());
    }
    
    /**
     * Remove follower relationship completely (hard delete)
     */
    public void removeFollowerRelationship(Long customerId, Long vendorId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + vendorId));
        
        Optional<Follower> follower = followerRepository.findByCustomerAndVendor(customer, vendor);
        follower.ifPresent(followerRepository::delete);
    }
}
