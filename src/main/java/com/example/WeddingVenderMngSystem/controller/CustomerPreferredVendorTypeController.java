package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.CustomerPreferredVendorTypeDTO;
import com.example.WeddingVenderMngSystem.entity.CustomerPreferredVendorType;
import com.example.WeddingVenderMngSystem.service.CustomerPreferredVendorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer-preferred-vendor-types")
@CrossOrigin(origins = "*")
public class CustomerPreferredVendorTypeController {
    
    @Autowired
    private CustomerPreferredVendorTypeService customerPreferredVendorTypeService;
    
    /**
     * Add a preferred vendor type for a customer
     */
    @PostMapping("/customer/{customerId}/vendor-type")
    public ResponseEntity<?> addPreferredVendorType(
            @PathVariable Long customerId, 
            @RequestParam String vendorType) {
        try {
            CustomerPreferredVendorType preference = customerPreferredVendorTypeService.addPreferredVendorType(customerId, vendorType);
            CustomerPreferredVendorTypeDTO dto = convertToDTO(preference);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding preferred vendor type: " + e.getMessage());
        }
    }
    
    /**
     * Add multiple preferred vendor types for a customer
     */
    @PostMapping("/customer/{customerId}/vendor-types")
    public ResponseEntity<?> addMultiplePreferredVendorTypes(
            @PathVariable Long customerId, 
            @RequestBody List<String> vendorTypes) {
        try {
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.addMultiplePreferredVendorTypes(customerId, vendorTypes);
            List<CustomerPreferredVendorTypeDTO> dtos = preferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Get all preferred vendor types for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerPreferredVendorTypes(@PathVariable Long customerId) {
        try {
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.getCustomerPreferredVendorTypes(customerId);
            List<CustomerPreferredVendorTypeDTO> dtos = preferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Get all preferred vendor type names for a customer
     */
    @GetMapping("/customer/{customerId}/vendor-type-names")
    public ResponseEntity<?> getCustomerPreferredVendorTypeNames(@PathVariable Long customerId) {
        try {
            List<String> vendorTypeNames = customerPreferredVendorTypeService.getCustomerPreferredVendorTypeNames(customerId);
            return ResponseEntity.ok(vendorTypeNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching preferred vendor type names: " + e.getMessage());
        }
    }
    
    /**
     * Get all customers who prefer a specific vendor type
     */
    @GetMapping("/vendor-type/{vendorType}/customers")
    public ResponseEntity<?> getCustomersByVendorType(@PathVariable String vendorType) {
        try {
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.getCustomersByVendorType(vendorType);
            List<CustomerPreferredVendorTypeDTO> dtos = preferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching customers by vendor type: " + e.getMessage());
        }
    }
    
    /**
     * Remove a specific preferred vendor type for a customer
     */
    @DeleteMapping("/customer/{customerId}/vendor-type")
    public ResponseEntity<?> removePreferredVendorType(
            @PathVariable Long customerId, 
            @RequestParam String vendorType) {
        try {
            customerPreferredVendorTypeService.removePreferredVendorType(customerId, vendorType);
            return ResponseEntity.ok("Preferred vendor type removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing preferred vendor type: " + e.getMessage());
        }
    }
    
    /**
     * Remove all preferred vendor types for a customer
     */
    @DeleteMapping("/customer/{customerId}/all")
    public ResponseEntity<?> removeAllPreferredVendorTypes(@PathVariable Long customerId) {
        try {
            customerPreferredVendorTypeService.removeAllPreferredVendorTypes(customerId);
            return ResponseEntity.ok("All preferred vendor types removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing all preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Update customer's preferred vendor types (replace all existing with new ones)
     */
    @PutMapping("/customer/{customerId}")
    public ResponseEntity<?> updateCustomerPreferredVendorTypes(
            @PathVariable Long customerId, 
            @RequestBody List<String> vendorTypes) {
        try {
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.updateCustomerPreferredVendorTypes(customerId, vendorTypes);
            List<CustomerPreferredVendorTypeDTO> dtos = preferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Check if a customer has a specific vendor type preference
     */
    @GetMapping("/customer/{customerId}/has-vendor-type")
    public ResponseEntity<?> hasVendorTypePreference(
            @PathVariable Long customerId, 
            @RequestParam String vendorType) {
        try {
            boolean hasPreference = customerPreferredVendorTypeService.hasVendorTypePreference(customerId, vendorType);
            return ResponseEntity.ok(hasPreference);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking vendor type preference: " + e.getMessage());
        }
    }
    
    /**
     * Get all distinct vendor types from customer preferences
     */
    @GetMapping("/vendor-types/distinct")
    public ResponseEntity<?> getAllDistinctVendorTypes() {
        try {
            List<String> vendorTypes = customerPreferredVendorTypeService.getAllDistinctVendorTypes();
            return ResponseEntity.ok(vendorTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching distinct vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Get statistics of how many customers prefer each vendor type
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getVendorTypeStatistics() {
        try {
            List<Object[]> statistics = customerPreferredVendorTypeService.getVendorTypeStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching vendor type statistics: " + e.getMessage());
        }
    }
    
    /**
     * Sync customer preferred vendor types from old system to new table (for data migration)
     */
    @PostMapping("/sync/customer/{customerId}")
    public ResponseEntity<?> syncCustomerPreferredVendorTypes(@PathVariable Long customerId) {
        try {
            customerPreferredVendorTypeService.syncCustomerPreferredVendorTypes(customerId);
            return ResponseEntity.ok("Customer preferred vendor types synced successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error syncing customer preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Sync all customers' preferred vendor types from old system to new table
     */
    @PostMapping("/sync/all")
    public ResponseEntity<?> syncAllCustomerPreferredVendorTypes() {
        try {
            customerPreferredVendorTypeService.syncAllCustomerPreferredVendorTypes();
            return ResponseEntity.ok("All customer preferred vendor types synced successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error syncing all customer preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Convert entity to DTO
     */
    private CustomerPreferredVendorTypeDTO convertToDTO(CustomerPreferredVendorType entity) {
        return new CustomerPreferredVendorTypeDTO(
                entity.getId(),
                entity.getVendorType(),
                entity.getCustomer().getCustomerId()
        );
    }
}
