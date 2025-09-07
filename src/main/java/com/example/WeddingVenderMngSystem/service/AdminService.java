package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.AdminCompleteInfoResponse;
import com.example.WeddingVenderMngSystem.entity.Admin;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public AdminCompleteInfoResponse getAdminCompleteInfo(Long adminId) {
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found with ID: " + adminId);
        }
        
        Admin admin = adminOpt.get();
        User user = admin.getUser();
        
        // Update last login time
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
        
        return mapToAdminCompleteInfoResponse(admin, user);
    }

    public AdminCompleteInfoResponse getAdminCompleteInfoByUserId(Long userId) {
        Optional<Admin> adminOpt = adminRepository.findByUserId(userId);
        
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found for user ID: " + userId);
        }
        
        Admin admin = adminOpt.get();
        User user = admin.getUser();
        
        // Update last login time
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
        
        return mapToAdminCompleteInfoResponse(admin, user);
    }

    private AdminCompleteInfoResponse mapToAdminCompleteInfoResponse(Admin admin, User user) {
        AdminCompleteInfoResponse response = new AdminCompleteInfoResponse();
        
        // Admin specific fields
        response.setAdminId(admin.getAdminId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setPhoneNumber(admin.getPhoneNumber());
        response.setAddress(admin.getAddress());
        response.setLastLogin(admin.getLastLogin());
        response.setActive(admin.getActive());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());
        
        // User fields
        if (user != null) {
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
        }
        
        // Count managed vendors
        response.setManagedVendorsCount(
            admin.getManagedVendors() != null ? admin.getManagedVendors().size() : 0
        );
        
        return response;
    }
}
