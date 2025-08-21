package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.AdminCompleteInfoResponse;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.service.AdminService;
import com.example.WeddingVenderMngSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private VendorService vendorService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome Admin!";
    }

    @GetMapping("/admin-complete-info")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCompleteInfoResponse> getAdminCompleteInfo() {
        try {
            // Get the current authenticated user ID
            Long userId = getCurrentUserId();
            
            AdminCompleteInfoResponse response = adminService.getAdminCompleteInfoByUserId(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin-complete-info/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCompleteInfoResponse> getAdminCompleteInfoById(@PathVariable Long adminId) {
        try {
            AdminCompleteInfoResponse response = adminService.getAdminCompleteInfo(adminId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/vendors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        try {
            List<Vendor> vendors = vendorService.getAllVendors();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/vendors/{vendorId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vendor> updateVendorVerification(@PathVariable Long vendorId, @RequestParam Boolean verify) {
        try {
            Vendor updatedVendor = vendorService.updateVendorVerificationStatus(vendorId, verify);
            return ResponseEntity.ok(updatedVendor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to get current user ID - you'll need to implement this based on your JWT/Security setup
    private Long getCurrentUserId() {
        // This is a placeholder - implement based on your authentication mechanism
        // You might extract this from JWT token or get it from UserDetails
        // For now, returning a placeholder value
        return 1L; // TODO: Replace with actual implementation based on your security setup
    }
}
