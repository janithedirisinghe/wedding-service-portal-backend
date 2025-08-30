package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.VendorStatsDTO;
import com.example.WeddingVenderMngSystem.dto.VendorUpdateDTO;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import com.example.WeddingVenderMngSystem.repository.FollowerRepository;
import com.example.WeddingVenderMngSystem.repository.PostRepository;
import com.example.WeddingVenderMngSystem.repository.ReviewRepository;
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
    private FollowerRepository followerRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

    // Get all vendors for admin
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // Update vendor verification status for admin
    public Vendor updateVendorVerificationStatus(Long vendorId, Boolean verifyStatus) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + vendorId));
        
        vendor.setVerify(verifyStatus);
        return vendorRepository.save(vendor);
    }

    // ================= SEARCH METHODS =================
    
    // Basic Search Methods
    public List<Vendor> searchByBusinessName(String businessName) {
        return vendorRepository.findByBusinessNameContainingIgnoreCase(businessName);
    }
    
    public List<Vendor> searchByVendorType(String venType) {
        return vendorRepository.findByVenTypeIgnoreCase(venType);
    }
    
    public List<Vendor> searchByLocation(String location) {
        return vendorRepository.findByLocationContainingIgnoreCase(location);
    }
    
    public List<Vendor> searchByCountry(String country) {
        return vendorRepository.findByCountryIgnoreCase(country);
    }
    
    public List<Vendor> searchByAvailability(String availability) {
        return vendorRepository.findByAvailabilityIgnoreCase(availability);
    }
    
    public List<Vendor> getActiveVendors() {
        return vendorRepository.findByIsActiveTrue();
    }
    
    public List<Vendor> getVerifiedVendors() {
        return vendorRepository.findByVerifyTrue();
    }
    
    public List<Vendor> getActiveAndVerifiedVendors() {
        return vendorRepository.findByIsActiveTrueAndVerifyTrue();
    }
    
    // Advanced Search Methods
    public List<Vendor> searchByTypeAndLocation(String venType, String location) {
        return vendorRepository.findByVenTypeIgnoreCaseAndLocationContainingIgnoreCase(venType, location);
    }
    
    public List<Vendor> searchByLocationAndCountry(String location, String country) {
        return vendorRepository.findByLocationContainingIgnoreCaseAndCountryIgnoreCase(location, country);
    }
    
    public List<Vendor> searchByTypeAndCountry(String venType, String country) {
        return vendorRepository.findByVenTypeIgnoreCaseAndCountryIgnoreCase(venType, country);
    }
    
    // Verified Vendor Search
    public List<Vendor> searchVerifiedVendorsByType(String venType) {
        return vendorRepository.findByVenTypeIgnoreCaseAndIsActiveTrueAndVerifyTrue(venType);
    }
    
    public List<Vendor> searchVerifiedVendorsByLocation(String location) {
        return vendorRepository.findByLocationContainingIgnoreCaseAndIsActiveTrueAndVerifyTrue(location);
    }
    
    public List<Vendor> searchVerifiedVendorsByCountry(String country) {
        return vendorRepository.findByCountryIgnoreCaseAndIsActiveTrueAndVerifyTrue(country);
    }
    
    // Bio/Description Search
    public List<Vendor> searchByBio(String keyword) {
        return vendorRepository.findByBioContainingIgnoreCase(keyword);
    }
    
    public List<Vendor> searchVerifiedVendorsByBio(String keyword) {
        return vendorRepository.findByBioContainingIgnoreCaseAndIsActiveTrueAndVerifyTrue(keyword);
    }
    
    // Multi-criteria Search
    public List<Vendor> searchVendors(String businessName, String venType, String location, 
                                     String country, String availability, Boolean isActive, Boolean verify) {
        return vendorRepository.searchVendors(businessName, venType, location, country, availability, isActive, verify);
    }
    
    // Service-based Search
    public List<Vendor> searchByServiceName(String serviceName) {
        return vendorRepository.findByServiceNameContaining(serviceName);
    }
    
    public List<Vendor> searchByServicePriceRange(Double minPrice, Double maxPrice) {
        return vendorRepository.findByServicePriceRange(minPrice, maxPrice);
    }
    
    public List<Vendor> searchByServicePricingModel(String pricingModel) {
        return vendorRepository.findByServicePricingModel(pricingModel);
    }
    
    // Follower-based Search
    public List<Vendor> getVendorsWithMinimumFollowers(int minFollowers) {
        return vendorRepository.findVendorsWithMinimumFollowers(minFollowers);
    }
    
    public List<Vendor> getMostFollowedVendors(int limit) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        return vendorRepository.findMostFollowedVendors(pageable);
    }
    
    // Auto-complete Search
    public List<String> getBusinessNameSuggestions(String query, int limit) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        return vendorRepository.findBusinessNameSuggestions(query, pageable);
    }
    
    public List<String> getVenTypeSuggestions(String query, int limit) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        return vendorRepository.findVenTypeSuggestions(query, pageable);
    }
    
    public List<String> getLocationSuggestions(String query, int limit) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        return vendorRepository.findLocationSuggestions(query, pageable);
    }
    
    // Get vendor statistics (review count, post count, follower count)
    public VendorStatsDTO getVendorStats(Long userId) {
        // Get vendor by userId
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));
        
        Long vendorId = vendor.getVenderId();
        
        // Get counts
        Long reviewCount = reviewRepository.countByVendorId(vendorId);
        Long postCount = postRepository.countByVendorId(vendorId);
        Long followerCount = followerRepository.countActiveFollowersByVendorId(vendorId);
        
        return new VendorStatsDTO(reviewCount, postCount, followerCount);
    }

}
