package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.VendorUpdateDTO;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    public Vendor registerVendor(Long userId, Vendor vendorDetails) {
        // Check if user exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }

        User user = userOptional.get();

        // Check if user is already linked to a vendor
        if (user.getVendor() != null) {
            throw new IllegalStateException("Vendor already exists for this user!");
        }

        // Assign user to vendor
        vendorDetails.setUser(user);

        // Save vendor details
        vendorRepository.save(vendorDetails);

        return vendorRepository.save(vendorDetails);
    }

    public Vendor getVendorById(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + vendorId));
    }

    public Vendor getVendorByUserId(Long userId) {
        return vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));
    }

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<com.example.WeddingVenderMngSystem.entity.Service> getServicesByVendorId(Long vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);
        return vendor.map(Vendor::getServices).orElse(null); // Return services if found
    }

    public List<com.example.WeddingVenderMngSystem.entity.Service> getServicesByUserId(Long userId) {
        Optional<Vendor> vendor = vendorRepository.findByUser_UserId(userId);
        return vendor.map(Vendor::getServices).orElse(null); // Return services if found
    }

    public Vendor updateVendorProfile(Long userId, VendorUpdateDTO vendorUpdateDTO) {
        // Find the vendor by user ID
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        // Update vendor fields with new values
        if (vendorUpdateDTO.getBusinessName() != null) {
            vendor.setBusinessName(vendorUpdateDTO.getBusinessName());
        }
        if (vendorUpdateDTO.getAvailability() != null) {
            vendor.setAvailability(vendorUpdateDTO.getAvailability());
        }
        if (vendorUpdateDTO.getLocation() != null) {
            vendor.setLocation(vendorUpdateDTO.getLocation());
        }
        if (vendorUpdateDTO.getBRN() != null) {
            vendor.setBRN(vendorUpdateDTO.getBRN());
        }
        if (vendorUpdateDTO.getCountry() != null) {
            vendor.setCountry(vendorUpdateDTO.getCountry());
        }
        if (vendorUpdateDTO.getVenType() != null) {
            vendor.setVenType(vendorUpdateDTO.getVenType());
        }
        if (vendorUpdateDTO.getBio() != null) {
            vendor.setBio(vendorUpdateDTO.getBio());
        }
        if (vendorUpdateDTO.getTelNo() != null) {
            vendor.setTelNo(vendorUpdateDTO.getTelNo());
        }
        if (vendorUpdateDTO.getProfileImageUrl() != null) {
            vendor.setProfileImageUrl(vendorUpdateDTO.getProfileImageUrl());
        }

        // Save and return the updated vendor
        return vendorRepository.save(vendor);
    }

    public Vendor updateVendorProfileWithImage(Long userId, VendorUpdateDTO vendorUpdateDTO, MultipartFile profileImage) {
        // Find the vendor by user ID
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        // Update vendor fields with new values
        if (vendorUpdateDTO.getBusinessName() != null) {
            vendor.setBusinessName(vendorUpdateDTO.getBusinessName());
        }
        if (vendorUpdateDTO.getAvailability() != null) {
            vendor.setAvailability(vendorUpdateDTO.getAvailability());
        }
        if (vendorUpdateDTO.getLocation() != null) {
            vendor.setLocation(vendorUpdateDTO.getLocation());
        }
        if (vendorUpdateDTO.getBRN() != null) {
            vendor.setBRN(vendorUpdateDTO.getBRN());
        }
        if (vendorUpdateDTO.getCountry() != null) {
            vendor.setCountry(vendorUpdateDTO.getCountry());
        }
        if (vendorUpdateDTO.getVenType() != null) {
            vendor.setVenType(vendorUpdateDTO.getVenType());
        }
        if (vendorUpdateDTO.getBio() != null) {
            vendor.setBio(vendorUpdateDTO.getBio());
        }
        if (vendorUpdateDTO.getTelNo() != null) {
            vendor.setTelNo(vendorUpdateDTO.getTelNo());
        }

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String fileName = "vendor_profile_" + userId + "_" + System.currentTimeMillis() + "_" + 
                                profileImage.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String imageUrl = supabaseStorageService.uploadFile(profileImage, fileName);
                vendor.setProfileImageUrl(imageUrl);
                vendorUpdateDTO.setProfileImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
            }
        }

        // Save and return the updated vendor
        return vendorRepository.save(vendor);
    }

}
