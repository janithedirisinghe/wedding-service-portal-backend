package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.CustomerDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

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
}
