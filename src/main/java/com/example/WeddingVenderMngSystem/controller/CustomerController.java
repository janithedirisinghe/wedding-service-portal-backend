package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.CustomerDTO;
import com.example.WeddingVenderMngSystem.dto.CustomerStatsDTO;
import com.example.WeddingVenderMngSystem.dto.VendorSuggestionRequestDTO;
import com.example.WeddingVenderMngSystem.dto.VendorSuggestionResponseDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.service.CustomerService;
import com.example.WeddingVenderMngSystem.service.VendorSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private VendorSuggestionService vendorSuggestionService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long userId) {
        try {
            CustomerDTO customer = customerService.getCustomerDTOByUserId(userId);
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@PathVariable Long userId) {
        try {
            CustomerDTO customer = customerService.getCustomerDTOByUserId(userId);
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<CustomerStatsDTO> getCustomerStats(@PathVariable Long userId) {
        try {
            CustomerStatsDTO stats = customerService.getCustomerStatsByUserId(userId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update customer profile with JSON data (without file upload)
    @PutMapping(value = "/editCustomer/{userId}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long userId, 
            @RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO updatedCustomer = customerService.updateCustomerByUserId(userId, customerDTO);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Update customer profile with file upload (multipart/form-data)
    @PutMapping(value = "/editCustomerWithImage/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomerDTO> updateCustomerWithImage(
            @PathVariable Long userId, 
            @RequestPart("customer") CustomerDTO customerDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            CustomerDTO updatedCustomer;
            if (profileImage != null && !profileImage.isEmpty()) {
                updatedCustomer = customerService.updateCustomerWithImageByUserId(userId, customerDTO, profileImage);
            } else {
                updatedCustomer = customerService.updateCustomerByUserId(userId, customerDTO);
            }
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable Long userId) {
        try {
            customerService.deleteCustomerByUserId(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Customer deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/entity/{userId}")
    public ResponseEntity<Customer> getCustomerEntityById(@PathVariable Long userId) {
        try {
            Customer customer = customerService.getCustomerByUserId(userId);
            return ResponseEntity.ok(customer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get vendor suggestions for a specific user using their user ID
     * This is the preferred method as it uses userId from authentication
     * 
     * @param userId The user's ID from authentication
     * @param request VendorSuggestionRequestDTO containing additional preferences
     * @return VendorSuggestionResponseDTO with suggested vendors
     */
    @PostMapping("/users/{userId}/suggest-vendors")
    public ResponseEntity<VendorSuggestionResponseDTO> suggestVendorsByUserId(
            @PathVariable Long userId,
            @RequestBody VendorSuggestionRequestDTO request) {
        
        try {
            VendorSuggestionResponseDTO response = vendorSuggestionService.suggestVendorsByUserId(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            VendorSuggestionResponseDTO errorResponse = new VendorSuggestionResponseDTO();
            errorResponse.setSuggestedVendors(new ArrayList<>());
            errorResponse.setTotalSuggestions(0);
            errorResponse.setMessage("Error occurred while suggesting vendors: " + e.getMessage());
            errorResponse.setAppliedFilters(new ArrayList<>());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Get vendor suggestions for a specific user using their user ID with query parameters
     * 
     * @param userId The user's ID from authentication
     * @param location Optional location override
     * @param budget Optional budget override
     * @param preferredTypes Optional preferred vendor types
     * @param sortBy Optional sort criteria
     * @param limit Optional result limit
     * @param minRating Optional minimum rating filter
     * @return VendorSuggestionResponseDTO with suggested vendors
     */
    @GetMapping("/users/{userId}/suggest-vendors")
    public ResponseEntity<VendorSuggestionResponseDTO> suggestVendorsByUserIdWithParams(
            @PathVariable Long userId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double budget,
            @RequestParam(required = false) List<String> preferredTypes,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer minRating) {
        
        try {
            VendorSuggestionRequestDTO request = new VendorSuggestionRequestDTO();
            request.setCustomerLocation(location);
            request.setBudget(budget);
            request.setPreferredVendorTypes(preferredTypes);
            request.setSortBy(sortBy);
            request.setLimit(limit);
            request.setMinRating(minRating);
            
            VendorSuggestionResponseDTO response = vendorSuggestionService.suggestVendorsByUserId(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            VendorSuggestionResponseDTO errorResponse = new VendorSuggestionResponseDTO();
            errorResponse.setSuggestedVendors(new ArrayList<>());
            errorResponse.setTotalSuggestions(0);
            errorResponse.setMessage("Error occurred while suggesting vendors: " + e.getMessage());
            errorResponse.setAppliedFilters(new ArrayList<>());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Suggest vendors based on customer preferences and details
     * 
     * @param request VendorSuggestionRequestDTO containing customer preferences
     * @return VendorSuggestionResponseDTO with suggested vendors
     */
    @PostMapping("/suggest-vendors")
    public ResponseEntity<VendorSuggestionResponseDTO> suggestVendors(@RequestBody VendorSuggestionRequestDTO request) {
        try {
            VendorSuggestionResponseDTO response = vendorSuggestionService.suggestVendors(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            VendorSuggestionResponseDTO errorResponse = new VendorSuggestionResponseDTO();
            errorResponse.setSuggestedVendors(new ArrayList<>());
            errorResponse.setTotalSuggestions(0);
            errorResponse.setMessage("Error occurred while suggesting vendors: " + e.getMessage());
            errorResponse.setAppliedFilters(new ArrayList<>());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Get vendor suggestions for a specific customer using their customer ID
     * 
     * @param customerId The customer's ID
     * @param location Optional location override
     * @param budget Optional budget override
     * @param preferredTypes Optional preferred vendor types
     * @param sortBy Optional sort criteria
     * @param limit Optional result limit
     * @return VendorSuggestionResponseDTO with suggested vendors
     */
    @GetMapping("/{customerId}/suggest-vendors")
    public ResponseEntity<VendorSuggestionResponseDTO> suggestVendorsForCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double budget,
            @RequestParam(required = false) List<String> preferredTypes,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer limit) {
        
        try {
            VendorSuggestionRequestDTO request = new VendorSuggestionRequestDTO();
            request.setCustomerId(customerId);
            request.setCustomerLocation(location);
            request.setBudget(budget);
            request.setPreferredVendorTypes(preferredTypes);
            request.setSortBy(sortBy);
            request.setLimit(limit);
            
            VendorSuggestionResponseDTO response = vendorSuggestionService.suggestVendors(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            VendorSuggestionResponseDTO errorResponse = new VendorSuggestionResponseDTO();
            errorResponse.setSuggestedVendors(new ArrayList<>());
            errorResponse.setTotalSuggestions(0);
            errorResponse.setMessage("Error occurred while suggesting vendors: " + e.getMessage());
            errorResponse.setAppliedFilters(new ArrayList<>());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
