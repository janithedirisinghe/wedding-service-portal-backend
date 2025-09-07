package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.CustomerPreferredVendorTypeDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.CustomerPreferredVendorType;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.service.CustomerPreferredVendorTypeService;
import com.example.WeddingVenderMngSystem.service.CustomerService;
import com.example.WeddingVenderMngSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer-preferred-vendor-types")
@CrossOrigin(origins = "*")
public class CustomerPreferredVendorTypeController {
    
    @Autowired
    private CustomerPreferredVendorTypeService customerPreferredVendorTypeService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get preferred vendor types for the currently authenticated user
     */
    @GetMapping("/my-preferences")
    public ResponseEntity<?> getMyPreferredVendorTypes() {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.getCustomerPreferredVendorTypes(customer.getCustomerId());
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
     * Get preferred vendor type names only for the currently authenticated user
     */
    @GetMapping("/my-preferences/names")
    public ResponseEntity<?> getMyPreferredVendorTypeNames() {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            List<String> vendorTypeNames = customerPreferredVendorTypeService.getCustomerPreferredVendorTypeNames(customer.getCustomerId());
            return ResponseEntity.ok(vendorTypeNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching preferred vendor type names: " + e.getMessage());
        }
    }
    
    /**
     * Add a preferred vendor type for the currently authenticated user
     */
    @PostMapping("/my-preferences/vendor-type")
    public ResponseEntity<?> addMyPreferredVendorType(@RequestParam String vendorType) {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            CustomerPreferredVendorType preference = customerPreferredVendorTypeService.addPreferredVendorType(customer.getCustomerId(), vendorType);
            CustomerPreferredVendorTypeDTO dto = convertToDTO(preference);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding preferred vendor type: " + e.getMessage());
        }
    }
    
    /**
     * Add multiple preferred vendor types for the currently authenticated user
     */
    @PostMapping("/my-preferences/vendor-types")
    public ResponseEntity<?> addMyMultiplePreferredVendorTypes(@RequestBody List<String> vendorTypes) {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.addMultiplePreferredVendorTypes(customer.getCustomerId(), vendorTypes);
            List<CustomerPreferredVendorTypeDTO> dtos = preferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Update all preferred vendor types for the currently authenticated user (replace existing)
     */
    @PutMapping("/my-preferences")
    public ResponseEntity<?> updateMyPreferredVendorTypes(@RequestBody List<String> vendorTypes) {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.updateCustomerPreferredVendorTypes(customer.getCustomerId(), vendorTypes);
            List<CustomerPreferredVendorTypeDTO> dtos = preferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Remove a specific preferred vendor type for the currently authenticated user
     */
    @DeleteMapping("/my-preferences/vendor-type")
    public ResponseEntity<?> removeMyPreferredVendorType(@RequestParam String vendorType) {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            customerPreferredVendorTypeService.removePreferredVendorType(customer.getCustomerId(), vendorType);
            return ResponseEntity.ok("Preferred vendor type removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing preferred vendor type: " + e.getMessage());
        }
    }
    
    /**
     * Remove all preferred vendor types for the currently authenticated user
     */
    @DeleteMapping("/my-preferences/all")
    public ResponseEntity<?> removeAllMyPreferredVendorTypes() {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            customerPreferredVendorTypeService.removeAllPreferredVendorTypes(customer.getCustomerId());
            return ResponseEntity.ok("All preferred vendor types removed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing all preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * Check if the currently authenticated user has a specific vendor type preference
     */
    @GetMapping("/my-preferences/has-vendor-type")
    public ResponseEntity<?> doIHaveVendorTypePreference(@RequestParam String vendorType) {
        try {
            Long userId = getCurrentUserId();
            Customer customer = customerService.getCustomerByUserId(userId);
            
            boolean hasPreference = customerPreferredVendorTypeService.hasVendorTypePreference(customer.getCustomerId(), vendorType);
            return ResponseEntity.ok(hasPreference);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking vendor type preference: " + e.getMessage());
        }
    }
    
    // ========== ADMIN ENDPOINTS ==========
    
    /**
     * ADMIN: Get all customer preferred vendor types
     */
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllCustomerPreferredVendorTypes() {
        try {
            // Here you might want to add admin authentication check
            List<CustomerPreferredVendorType> allPreferences = customerPreferredVendorTypeService.getAllCustomerPreferredVendorTypes();
            List<CustomerPreferredVendorTypeDTO> dtos = allPreferences.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching all customer preferred vendor types: " + e.getMessage());
        }
    }
    
    /**
     * ADMIN: Get preferred vendor types for a specific customer by user ID
     */
    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<?> getCustomerPreferredVendorTypesByUserId(@PathVariable Long userId) {
        try {
            Customer customer = customerService.getCustomerByUserId(userId);
            List<CustomerPreferredVendorType> preferences = customerPreferredVendorTypeService.getCustomerPreferredVendorTypes(customer.getCustomerId());
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
     * ADMIN: Get preferred vendor types for a specific customer by customer ID
     */
    @GetMapping("/admin/customer/{customerId}")
    public ResponseEntity<?> getAdminCustomerPreferredVendorTypes(@PathVariable Long customerId) {
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
    
    // ========== ANALYTICS ENDPOINTS ==========
    
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
    
    // ========== DATA MIGRATION ENDPOINTS ==========
    
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
    
    // ========== HELPER METHODS ==========
    
    /**
     * Get current user ID from security context
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                return user.getUserId();
            }
        }
        throw new RuntimeException("User not authenticated");
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
