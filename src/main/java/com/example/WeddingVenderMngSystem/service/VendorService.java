package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

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

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<com.example.WeddingVenderMngSystem.entity.Service> getServicesByVendorId(Long vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);
        return vendor.map(Vendor::getServices).orElse(null); // Return services if found
    }

}
