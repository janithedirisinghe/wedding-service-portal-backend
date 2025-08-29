package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.CustomerPreferredVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPreferredVendorTypeRepository extends JpaRepository<CustomerPreferredVendorType, Long> {
    
    /**
     * Find all preferred vendor types for a specific customer
     */
    List<CustomerPreferredVendorType> findByCustomer(Customer customer);
    
    /**
     * Find all preferred vendor types for a customer by customer ID
     */
    @Query("SELECT cpvt FROM CustomerPreferredVendorType cpvt WHERE cpvt.customer.customerId = :customerId")
    List<CustomerPreferredVendorType> findByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Find all customers who prefer a specific vendor type
     */
    List<CustomerPreferredVendorType> findByVendorType(String vendorType);
    
    /**
     * Check if a customer has a specific vendor type preference
     */
    @Query("SELECT COUNT(cpvt) > 0 FROM CustomerPreferredVendorType cpvt WHERE cpvt.customer.customerId = :customerId AND cpvt.vendorType = :vendorType")
    boolean existsByCustomerIdAndVendorType(@Param("customerId") Long customerId, @Param("vendorType") String vendorType);
    
    /**
     * Delete all preferences for a specific customer
     */
    void deleteByCustomer(Customer customer);
    
    /**
     * Delete all preferences for a customer by customer ID
     */
    @Query("DELETE FROM CustomerPreferredVendorType cpvt WHERE cpvt.customer.customerId = :customerId")
    void deleteByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Delete a specific preference for a customer
     */
    @Query("DELETE FROM CustomerPreferredVendorType cpvt WHERE cpvt.customer.customerId = :customerId AND cpvt.vendorType = :vendorType")
    void deleteByCustomerIdAndVendorType(@Param("customerId") Long customerId, @Param("vendorType") String vendorType);
    
    /**
     * Get distinct vendor types from all customer preferences
     */
    @Query("SELECT DISTINCT cpvt.vendorType FROM CustomerPreferredVendorType cpvt")
    List<String> findAllDistinctVendorTypes();
    
    /**
     * Count how many customers prefer each vendor type
     */
    @Query("SELECT cpvt.vendorType, COUNT(cpvt.customer) FROM CustomerPreferredVendorType cpvt GROUP BY cpvt.vendorType")
    List<Object[]> countCustomersByVendorType();
}
