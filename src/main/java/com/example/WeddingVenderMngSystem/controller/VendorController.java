package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.VendorStatsDTO;
import com.example.WeddingVenderMngSystem.dto.VendorUpdateDTO;
import com.example.WeddingVenderMngSystem.entity.Service;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/{userId}/services")
    public List<Service> getServicesByUser(@PathVariable Long userId) {
        return vendorService.getServicesByUserId(userId);
    }

    @GetMapping("/getvendor/{userId}")
    public Vendor getVendorByUserId(@PathVariable Long userId){
        Optional<Vendor> vendor = Optional.ofNullable(vendorService.getVendorByUserId(userId));
        if(vendor.isPresent()){
            return ResponseEntity.ok(vendor.get()).getBody();
        }else {
            return (Vendor) ResponseEntity.notFound();
        }

    }

    @PutMapping(value = "/updateprofile/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Vendor> updateVendorProfile(
            @PathVariable Long userId,
            @RequestPart("vendor") VendorUpdateDTO vendorUpdateDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            Vendor updatedVendor;
            if (profileImage != null && !profileImage.isEmpty()) {
                updatedVendor = vendorService.updateVendorProfileWithImage(userId, vendorUpdateDTO, profileImage);
            } else {
                updatedVendor = vendorService.updateVendorProfile(userId, vendorUpdateDTO);
            }
            return ResponseEntity.ok(updatedVendor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // New API: Get vendor by venderId
    @GetMapping("/getvenderByVenderId/{venderId}")
    public ResponseEntity<Vendor> getVendorByVenderId(@PathVariable Long venderId) {
        try {
            Vendor vendor = vendorService.getVendorById(venderId);
            return ResponseEntity.ok(vendor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ================= SEARCH APIs =================
    
    // Basic Search APIs
    @GetMapping("/search/business-name")
    public ResponseEntity<List<Vendor>> searchByBusinessName(@RequestParam String businessName) {
        try {
            List<Vendor> vendors = vendorService.searchByBusinessName(businessName);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/vendor-type")
    public ResponseEntity<List<Vendor>> searchByVendorType(@RequestParam String venType) {
        try {
            List<Vendor> vendors = vendorService.searchByVendorType(venType);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/location")
    public ResponseEntity<List<Vendor>> searchByLocation(@RequestParam String location) {
        try {
            List<Vendor> vendors = vendorService.searchByLocation(location);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/country")
    public ResponseEntity<List<Vendor>> searchByCountry(@RequestParam String country) {
        try {
            List<Vendor> vendors = vendorService.searchByCountry(country);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/availability")
    public ResponseEntity<List<Vendor>> searchByAvailability(@RequestParam String availability) {
        try {
            List<Vendor> vendors = vendorService.searchByAvailability(availability);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Status-based Search APIs
    @GetMapping("/search/active")
    public ResponseEntity<List<Vendor>> getActiveVendors() {
        try {
            List<Vendor> vendors = vendorService.getActiveVendors();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/verified")
    public ResponseEntity<List<Vendor>> getVerifiedVendors() {
        try {
            List<Vendor> vendors = vendorService.getVerifiedVendors();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/active-verified")
    public ResponseEntity<List<Vendor>> getActiveAndVerifiedVendors() {
        try {
            List<Vendor> vendors = vendorService.getActiveAndVerifiedVendors();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Advanced Search APIs
    @GetMapping("/search/type-location")
    public ResponseEntity<List<Vendor>> searchByTypeAndLocation(@RequestParam String venType, 
                                                               @RequestParam String location) {
        try {
            List<Vendor> vendors = vendorService.searchByTypeAndLocation(venType, location);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/location-country")
    public ResponseEntity<List<Vendor>> searchByLocationAndCountry(@RequestParam String location, 
                                                                  @RequestParam String country) {
        try {
            List<Vendor> vendors = vendorService.searchByLocationAndCountry(location, country);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/type-country")
    public ResponseEntity<List<Vendor>> searchByTypeAndCountry(@RequestParam String venType, 
                                                              @RequestParam String country) {
        try {
            List<Vendor> vendors = vendorService.searchByTypeAndCountry(venType, country);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Verified Vendor Search APIs
    @GetMapping("/search/verified/vendor-type")
    public ResponseEntity<List<Vendor>> searchVerifiedVendorsByType(@RequestParam String venType) {
        try {
            List<Vendor> vendors = vendorService.searchVerifiedVendorsByType(venType);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/verified/location")
    public ResponseEntity<List<Vendor>> searchVerifiedVendorsByLocation(@RequestParam String location) {
        try {
            List<Vendor> vendors = vendorService.searchVerifiedVendorsByLocation(location);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/verified/country")
    public ResponseEntity<List<Vendor>> searchVerifiedVendorsByCountry(@RequestParam String country) {
        try {
            List<Vendor> vendors = vendorService.searchVerifiedVendorsByCountry(country);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Bio/Description Search APIs
    @GetMapping("/search/bio")
    public ResponseEntity<List<Vendor>> searchByBio(@RequestParam String keyword) {
        try {
            List<Vendor> vendors = vendorService.searchByBio(keyword);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/verified/bio")
    public ResponseEntity<List<Vendor>> searchVerifiedVendorsByBio(@RequestParam String keyword) {
        try {
            List<Vendor> vendors = vendorService.searchVerifiedVendorsByBio(keyword);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Multi-criteria Search API
    @GetMapping("/search/advanced")
    public ResponseEntity<List<Vendor>> searchVendors(@RequestParam(required = false) String businessName,
                                                     @RequestParam(required = false) String venType,
                                                     @RequestParam(required = false) String location,
                                                     @RequestParam(required = false) String country,
                                                     @RequestParam(required = false) String availability,
                                                     @RequestParam(required = false) Boolean isActive,
                                                     @RequestParam(required = false) Boolean verify) {
        try {
            List<Vendor> vendors = vendorService.searchVendors(businessName, venType, location, 
                                                              country, availability, isActive, verify);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Service-based Search APIs
    @GetMapping("/search/service-name")
    public ResponseEntity<List<Vendor>> searchByServiceName(@RequestParam String serviceName) {
        try {
            List<Vendor> vendors = vendorService.searchByServiceName(serviceName);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/service-price-range")
    public ResponseEntity<List<Vendor>> searchByServicePriceRange(@RequestParam Double minPrice, 
                                                                 @RequestParam Double maxPrice) {
        try {
            List<Vendor> vendors = vendorService.searchByServicePriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/service-pricing-model")
    public ResponseEntity<List<Vendor>> searchByServicePricingModel(@RequestParam String pricingModel) {
        try {
            List<Vendor> vendors = vendorService.searchByServicePricingModel(pricingModel);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Follower-based Search APIs
    @GetMapping("/search/minimum-followers")
    public ResponseEntity<List<Vendor>> getVendorsWithMinimumFollowers(@RequestParam int minFollowers) {
        try {
            List<Vendor> vendors = vendorService.getVendorsWithMinimumFollowers(minFollowers);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/most-followed")
    public ResponseEntity<List<Vendor>> getMostFollowedVendors(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Vendor> vendors = vendorService.getMostFollowedVendors(limit);
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Auto-complete Search APIs
    @GetMapping("/search/suggestions/business-name")
    public ResponseEntity<List<String>> getBusinessNameSuggestions(@RequestParam String query, 
                                                                  @RequestParam(defaultValue = "5") int limit) {
        try {
            List<String> suggestions = vendorService.getBusinessNameSuggestions(query, limit);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/suggestions/vendor-type")
    public ResponseEntity<List<String>> getVenTypeSuggestions(@RequestParam String query, 
                                                             @RequestParam(defaultValue = "5") int limit) {
        try {
            List<String> suggestions = vendorService.getVenTypeSuggestions(query, limit);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search/suggestions/location")
    public ResponseEntity<List<String>> getLocationSuggestions(@RequestParam String query, 
                                                              @RequestParam(defaultValue = "5") int limit) {
        try {
            List<String> suggestions = vendorService.getLocationSuggestions(query, limit);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Combined Search API for Quick Filter
    @GetMapping("/search/quick")
    public ResponseEntity<List<Vendor>> quickSearch(@RequestParam String query) {
        try {
            // Search across business name, location, and vendor type
            List<Vendor> businessNameResults = vendorService.searchByBusinessName(query);
            List<Vendor> locationResults = vendorService.searchByLocation(query);
            List<Vendor> typeResults = vendorService.searchByVendorType(query);
            
            // Combine results and remove duplicates
            java.util.Set<Vendor> combinedResults = new java.util.LinkedHashSet<>();
            combinedResults.addAll(businessNameResults);
            combinedResults.addAll(locationResults);
            combinedResults.addAll(typeResults);
            
            return ResponseEntity.ok(new java.util.ArrayList<>(combinedResults));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get all vendor types for filter dropdown
    @GetMapping("/search/vendor-types")
    public ResponseEntity<List<String>> getAllVendorTypes() {
        try {
            List<Vendor> allVendors = vendorService.getActiveAndVerifiedVendors();
            List<String> vendorTypes = allVendors.stream()
                    .map(Vendor::getVenType)
                    .filter(type -> type != null && !type.trim().isEmpty())
                    .distinct()
                    .sorted()
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(vendorTypes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get all countries for filter dropdown
    @GetMapping("/search/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        try {
            List<Vendor> allVendors = vendorService.getActiveAndVerifiedVendors();
            List<String> countries = allVendors.stream()
                    .map(Vendor::getCountry)
                    .filter(country -> country != null && !country.trim().isEmpty())
                    .distinct()
                    .sorted()
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


//    @GetMapping("/{vendorId}")
//    public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long vendorId) {
//        Vendor vendor = vendorService.getVendorById(vendorId);
//
//        if (vendor == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Convert the List<Service> to List<ServiceDTO>
//        List<ServiceDTO> serviceDTOs = vendor.getServices().stream()
//                .map(this::convertToServiceDTO)
//                .collect(Collectors.toList());
//
//        // Create VendorDTO using the constructor that takes all fields
//        VendorDTO vendorDTO = new VendorDTO(
//                vendor.getVenderId(),
//                vendor.getBusinessName(),
//                vendor.getAvailability(),
//                vendor.getLocation(),
//                vendor.getBRN(),
//                vendor.getCountry(),
//                vendor.getVenType(),
//                vendor.getBio(),
//                vendor.getTelNo(),
//                vendor.getUser().getUsername(),
//                vendor.getUser().getEmail(),
//                serviceDTOs
//        );
//
//        return ResponseEntity.ok(vendorDTO);
//    }

    // Get vendor statistics (review count, post count, follower count)
    @GetMapping("/stats/{userId}")
    public ResponseEntity<VendorStatsDTO> getVendorStats(@PathVariable Long userId) {
        try {
            VendorStatsDTO stats = vendorService.getVendorStats(userId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
