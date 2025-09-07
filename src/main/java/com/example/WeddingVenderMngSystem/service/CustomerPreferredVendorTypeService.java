package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.CustomerPreferredVendorType;
import com.example.WeddingVenderMngSystem.repository.CustomerPreferredVendorTypeRepository;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerPreferredVendorTypeService {
    
    @Autowired
    private CustomerPreferredVendorTypeRepository customerPreferredVendorTypeRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * Add a preferred vendor type for a customer
     */
    public CustomerPreferredVendorType addPreferredVendorType(Long customerId, String vendorType) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        Customer customer = customerOpt.get();
        
        // Check if this preference already exists
        if (customerPreferredVendorTypeRepository.existsByCustomerIdAndVendorType(customerId, vendorType)) {
            throw new RuntimeException("Customer already has this vendor type preference");
        }
        
        CustomerPreferredVendorType preference = new CustomerPreferredVendorType(vendorType, customer);
        return customerPreferredVendorTypeRepository.save(preference);
    }
    
    /**
     * Add multiple preferred vendor types for a customer
     */
    public List<CustomerPreferredVendorType> addMultiplePreferredVendorTypes(Long customerId, List<String> vendorTypes) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        Customer customer = customerOpt.get();
        
        List<CustomerPreferredVendorType> preferences = vendorTypes.stream()
                .filter(vendorType -> !customerPreferredVendorTypeRepository.existsByCustomerIdAndVendorType(customerId, vendorType))
                .map(vendorType -> new CustomerPreferredVendorType(vendorType, customer))
                .collect(Collectors.toList());
        
        return customerPreferredVendorTypeRepository.saveAll(preferences);
    }
    
    /**
     * Get all customer preferred vendor types
     */
    public List<CustomerPreferredVendorType> getAllCustomerPreferredVendorTypes() {
        return customerPreferredVendorTypeRepository.findAll();
    }
    
    /**
     * Get all preferred vendor types for a customer
     */
    public List<CustomerPreferredVendorType> getCustomerPreferredVendorTypes(Long customerId) {
        return customerPreferredVendorTypeRepository.findByCustomerId(customerId);
    }
    
    /**
     * Get all preferred vendor type names for a customer
     */
    public List<String> getCustomerPreferredVendorTypeNames(Long customerId) {
        return customerPreferredVendorTypeRepository.findByCustomerId(customerId)
                .stream()
                .map(CustomerPreferredVendorType::getVendorType)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all customers who prefer a specific vendor type
     */
    public List<CustomerPreferredVendorType> getCustomersByVendorType(String vendorType) {
        return customerPreferredVendorTypeRepository.findByVendorType(vendorType);
    }
    
    /**
     * Remove a specific preferred vendor type for a customer
     */
    public void removePreferredVendorType(Long customerId, String vendorType) {
        customerPreferredVendorTypeRepository.deleteByCustomerIdAndVendorType(customerId, vendorType);
    }
    
    /**
     * Remove all preferred vendor types for a customer
     */
    public void removeAllPreferredVendorTypes(Long customerId) {
        customerPreferredVendorTypeRepository.deleteByCustomerId(customerId);
    }
    
    /**
     * Update customer's preferred vendor types (replace all existing with new ones)
     */
    public List<CustomerPreferredVendorType> updateCustomerPreferredVendorTypes(Long customerId, List<String> vendorTypes) {
        // Remove all existing preferences
        removeAllPreferredVendorTypes(customerId);
        
        // Add new preferences
        return addMultiplePreferredVendorTypes(customerId, vendorTypes);
    }
    
    /**
     * Check if a customer has a specific vendor type preference
     */
    public boolean hasVendorTypePreference(Long customerId, String vendorType) {
        return customerPreferredVendorTypeRepository.existsByCustomerIdAndVendorType(customerId, vendorType);
    }
    
    /**
     * Get all distinct vendor types from customer preferences
     */
    public List<String> getAllDistinctVendorTypes() {
        return customerPreferredVendorTypeRepository.findAllDistinctVendorTypes();
    }
    
    /**
     * Get statistics of how many customers prefer each vendor type
     */
    public List<Object[]> getVendorTypeStatistics() {
        return customerPreferredVendorTypeRepository.countCustomersByVendorType();
    }
    
    /**
     * Sync existing customer preferred vendor types from the old ElementCollection to new table
     * This method can be used for data migration
     */
    public void syncCustomerPreferredVendorTypes(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return;
        }
        
        Customer customer = customerOpt.get();
        List<String> existingPreferences = customer.getPreferredVendorTypes();
        
        if (existingPreferences != null && !existingPreferences.isEmpty()) {
            // Remove any existing new table entries for this customer
            removeAllPreferredVendorTypes(customerId);
            
            // Add all preferences from the old system to the new table
            addMultiplePreferredVendorTypes(customerId, existingPreferences);
        }
    }
    
    /**
     * Sync all customers' preferred vendor types from old system to new table
     */
    public void syncAllCustomerPreferredVendorTypes() {
        List<Customer> customers = customerRepository.findAll();
        
        for (Customer customer : customers) {
            syncCustomerPreferredVendorTypes(customer.getCustomerId());
        }
    }
}
