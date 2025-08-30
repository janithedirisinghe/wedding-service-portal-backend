package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.*;
import com.example.WeddingVenderMngSystem.entity.*;
import com.example.WeddingVenderMngSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendorSuggestionService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerPreferredVendorTypeRepository customerPreferredVendorTypeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public VendorSuggestionResponseDTO suggestVendors(VendorSuggestionRequestDTO request) {
        List<String> appliedFilters = new ArrayList<>();
        
        // Get customer details if customerId is provided
        final Customer customer;
        if (request.getCustomerId() != null) {
            Optional<Customer> customerOpt = customerRepository.findById(request.getCustomerId());
            if (customerOpt.isPresent()) {
                customer = customerOpt.get();
                appliedFilters.add("Customer profile preferences");
            } else {
                customer = null;
            }
        } else {
            customer = null;
        }

        return processVendorSuggestion(customer, request, appliedFilters);
    }

    public VendorSuggestionResponseDTO suggestVendorsByUserId(Long userId, VendorSuggestionRequestDTO request) {
        List<String> appliedFilters = new ArrayList<>();
        
        // Get customer details using userId
        final Customer customer;
        if (userId != null) {
            Optional<Customer> customerOpt = customerRepository.findByUser_UserId(userId);
            if (customerOpt.isPresent()) {
                customer = customerOpt.get();
                appliedFilters.add("Customer profile preferences from user ID: " + userId);
            } else {
                customer = null;
                appliedFilters.add("No customer profile found for user ID: " + userId);
            }
        } else {
            customer = null;
        }

        return processVendorSuggestion(customer, request, appliedFilters);
    }

    private VendorSuggestionResponseDTO processVendorSuggestion(Customer customer, VendorSuggestionRequestDTO request, List<String> appliedFilters) {
        
        // Determine preferred vendor types
        List<String> preferredTypes = getPreferredVendorTypes(customer, request.getPreferredVendorTypes());
        if (!preferredTypes.isEmpty()) {
            appliedFilters.add("Preferred vendor types: " + String.join(", ", preferredTypes));
        }

        // Determine location for search
        String searchLocation = getSearchLocation(customer, request.getCustomerLocation());
        if (searchLocation != null && !searchLocation.isEmpty()) {
            appliedFilters.add("Location: " + searchLocation);
        }

        // Determine budget constraints
        Double budgetMin = null;
        Double budgetMax = getBudgetConstraint(customer, request.getBudget());
        if (budgetMax != null) {
            appliedFilters.add("Budget constraint: ≤ $" + budgetMax);
        }

        // Get suggestion limit
        int limit = request.getLimit() != null ? request.getLimit() : 20;

        // Find suggested vendors
        List<Vendor> vendors = vendorRepository.findSuggestedVendors(
            preferredTypes.isEmpty() ? null : preferredTypes,
            searchLocation,
            null, // country - could be extracted from customer location
            budgetMin,
            budgetMax
        );

        // Convert to DTOs with additional calculations
        List<SuggestedVendorDTO> suggestedVendors = vendors.stream()
            .map(vendor -> convertToSuggestedVendorDTO(vendor, customer, request))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        // Apply rating filter if specified
        if (request.getMinRating() != null) {
            suggestedVendors = suggestedVendors.stream()
                .filter(v -> v.getAverageRating() == null || v.getAverageRating() >= request.getMinRating())
                .collect(Collectors.toList());
            appliedFilters.add("Minimum rating: " + request.getMinRating() + " stars");
        }

        // Sort the results based on criteria
        suggestedVendors = sortSuggestedVendors(suggestedVendors, request.getSortBy());

        // Limit results
        if (suggestedVendors.size() > limit) {
            suggestedVendors = suggestedVendors.subList(0, limit);
        }

        // Create response
        VendorSuggestionResponseDTO response = new VendorSuggestionResponseDTO();
        response.setSuggestedVendors(suggestedVendors);
        response.setTotalSuggestions(suggestedVendors.size());
        response.setAppliedFilters(appliedFilters);
        
        if (suggestedVendors.isEmpty()) {
            response.setMessage("No vendors found matching your criteria. Try adjusting your preferences or budget.");
        } else {
            response.setMessage("Found " + suggestedVendors.size() + " vendor suggestions based on your preferences.");
        }

        return response;
    }

    private List<String> getPreferredVendorTypes(Customer customer, List<String> requestTypes) {
        Set<String> types = new HashSet<>();
        
        // Add types from request
        if (requestTypes != null && !requestTypes.isEmpty()) {
            types.addAll(requestTypes);
        }
        
        // Add types from customer preferences
        if (customer != null) {
            List<CustomerPreferredVendorType> preferences = 
                customerPreferredVendorTypeRepository.findByCustomerId(customer.getCustomerId());
            types.addAll(preferences.stream()
                .map(CustomerPreferredVendorType::getVendorType)
                .collect(Collectors.toList()));
        }
        
        return new ArrayList<>(types);
    }

    private String getSearchLocation(Customer customer, String requestLocation) {
        if (requestLocation != null && !requestLocation.trim().isEmpty()) {
            return requestLocation.trim();
        }
        if (customer != null && customer.getLocation() != null && !customer.getLocation().trim().isEmpty()) {
            return customer.getLocation().trim();
        }
        return null;
    }

    private Double getBudgetConstraint(Customer customer, Double requestBudget) {
        if (requestBudget != null && requestBudget > 0) {
            return requestBudget;
        }
        if (customer != null && customer.getBudget() != null && !customer.getBudget().trim().isEmpty()) {
            try {
                // Try to parse budget string to double (assuming it might contain "5000" or "5000-10000")
                String budgetStr = customer.getBudget().trim();
                // Extract first number if it's a range like "5000-10000" or just "5000"
                String[] parts = budgetStr.split("-");
                return Double.parseDouble(parts[parts.length - 1]); // Use the higher value if it's a range
            } catch (NumberFormatException e) {
                // If parsing fails, return null
                return null;
            }
        }
        return null;
    }

    private SuggestedVendorDTO convertToSuggestedVendorDTO(Vendor vendor, Customer customer, VendorSuggestionRequestDTO request) {
        try {
            SuggestedVendorDTO dto = new SuggestedVendorDTO();
            dto.setVendorId(vendor.getVenderId());
            dto.setBusinessName(vendor.getBusinessName());
            dto.setVenType(vendor.getVenType());
            dto.setLocation(vendor.getLocation());
            dto.setCountry(vendor.getCountry());
            dto.setBio(vendor.getBio());
            dto.setTelNo(vendor.getTelNo());
            dto.setUserEmail(vendor.getUser() != null ? vendor.getUser().getEmail() : null);

            // Calculate average rating and review count
            Double avgRating = reviewRepository.averageRatingByVendorId(vendor.getVenderId());
            Long reviewCount = reviewRepository.countByVendorId(vendor.getVenderId());
            dto.setAverageRating(avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : null);
            dto.setReviewCount(reviewCount != null ? reviewCount : 0L);

            // Get services and calculate price range
            List<com.example.WeddingVenderMngSystem.entity.Service> services = vendor.getServices();
            if (services != null && !services.isEmpty()) {
                List<ServiceDTO> serviceDTOs = services.stream()
                    .map(this::convertServiceToDTO)
                    .collect(Collectors.toList());
                dto.setServices(serviceDTOs);

                // Calculate price range
                OptionalDouble minPrice = services.stream()
                    .filter(s -> s.getPricing() != null)
                    .mapToDouble(com.example.WeddingVenderMngSystem.entity.Service::getPricing)
                    .min();
                OptionalDouble maxPrice = services.stream()
                    .filter(s -> s.getPricing() != null)
                    .mapToDouble(com.example.WeddingVenderMngSystem.entity.Service::getPricing)
                    .max();
                
                dto.setMinServicePrice(minPrice.isPresent() ? minPrice.getAsDouble() : null);
                dto.setMaxServicePrice(maxPrice.isPresent() ? maxPrice.getAsDouble() : null);
            } else {
                dto.setServices(new ArrayList<>());
            }

            // Determine location match
            dto.setLocationMatch(determineLocationMatch(customer, vendor, request));

            // Determine match reason
            dto.setMatchReason(determineMatchReason(vendor, customer, request));

            // Calculate popularity score
            dto.setPopularityScore(calculatePopularityScore(vendor, avgRating, reviewCount));

            return dto;
        } catch (Exception e) {
            // Log error and return null to filter out problematic vendors
            System.err.println("Error converting vendor " + vendor.getVenderId() + " to DTO: " + e.getMessage());
            return null;
        }
    }

    private ServiceDTO convertServiceToDTO(com.example.WeddingVenderMngSystem.entity.Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceId(service.getServiceId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPricing(service.getPricing());
        dto.setVendorId(service.getVendor().getVenderId());
        return dto;
    }

    private String determineLocationMatch(Customer customer, Vendor vendor, VendorSuggestionRequestDTO request) {
        String customerLocation = getSearchLocation(customer, request.getCustomerLocation());
        String customerCountry = getCustomerCountry(customer);
        String vendorLocation = vendor.getLocation();
        String vendorCountry = vendor.getCountry();
        
        if (customerLocation == null && customerCountry == null) {
            return "location not specified";
        }
        
        // Check for exact location match (case-insensitive and flexible)
        if (isLocationMatch(customerLocation, vendorLocation)) {
            return "same location";
        }
        
        // Check for same country
        if (isLocationMatch(customerCountry, vendorCountry)) {
            return "same country";
        }
        
        // Check if vendor location contains customer location or vice versa
        if (customerLocation != null && vendorLocation != null) {
            String custLoc = normalizeLocation(customerLocation);
            String vendLoc = normalizeLocation(vendorLocation);
            
            if (custLoc.contains(vendLoc) || vendLoc.contains(custLoc)) {
                return "nearby location";
            }
        }
        
        return "different location";
    }
    
    private String getCustomerCountry(Customer customer) {
        if (customer != null && customer.getCountry() != null && !customer.getCountry().trim().isEmpty()) {
            return customer.getCountry().trim();
        }
        return null;
    }
    
    private boolean isLocationMatch(String location1, String location2) {
        if (location1 == null || location2 == null) {
            return false;
        }
        
        String loc1 = normalizeLocation(location1);
        String loc2 = normalizeLocation(location2);
        
        return loc1.equals(loc2);
    }
    
    private String normalizeLocation(String location) {
        if (location == null) {
            return "";
        }
        
        return location.toLowerCase()
            .trim()
            .replaceAll("\\s+", " ") // Replace multiple spaces with single space
            .replaceAll("[^a-z0-9\\s]", ""); // Remove special characters except spaces
    }

    private String determineMatchReason(Vendor vendor, Customer customer, VendorSuggestionRequestDTO request) {
        List<String> reasons = new ArrayList<>();
        
        // Check if vendor type matches preferences
        if (request.getPreferredVendorTypes() != null && 
            request.getPreferredVendorTypes().contains(vendor.getVenType())) {
            reasons.add("matches preferred vendor type");
        }
        
        // Check location match
        String searchLocation = getSearchLocation(customer, request.getCustomerLocation());
        String searchCountry = getCustomerCountry(customer);
        
        if (searchLocation != null && vendor.getLocation() != null &&
            isLocationMatch(searchLocation, vendor.getLocation())) {
            reasons.add("in your preferred location");
        } else if (searchCountry != null && vendor.getCountry() != null &&
            isLocationMatch(searchCountry, vendor.getCountry())) {
            reasons.add("in your country");
        }
        
        // Check rating
        Double avgRating = reviewRepository.averageRatingByVendorId(vendor.getVenderId());
        if (avgRating != null && avgRating >= 4.0) {
            reasons.add("highly rated (" + Math.round(avgRating * 10.0) / 10.0 + " stars)");
        }
        
        // Check budget
        Double budget = getBudgetConstraint(customer, request.getBudget());
        if (budget != null && vendor.getServices() != null) {
            boolean hasAffordableServices = vendor.getServices().stream()
                .anyMatch(s -> s.getPricing() != null && s.getPricing() <= budget);
            if (hasAffordableServices) {
                reasons.add("within your budget");
            }
        }
        
        if (reasons.isEmpty()) {
            return "verified active vendor";
        }
        
        return String.join(", ", reasons);
    }

    private Integer calculatePopularityScore(Vendor vendor, Double avgRating, Long reviewCount) {
        int score = 0;
        
        // Rating component (0-40 points)
        if (avgRating != null) {
            score += (int) (avgRating * 8); // 5 stars = 40 points
        }
        
        // Review count component (0-30 points)
        if (reviewCount != null) {
            score += Math.min(30, (int) (reviewCount * 2)); // Max 30 points for reviews
        }
        
        // Service variety component (0-20 points)
        if (vendor.getServices() != null) {
            score += Math.min(20, vendor.getServices().size() * 5); // 5 points per service, max 20
        }
        
        // Profile completeness (0-10 points)
        if (vendor.getBio() != null && !vendor.getBio().trim().isEmpty()) {
            score += 5;
        }
        if (vendor.getTelNo() != null && !vendor.getTelNo().trim().isEmpty()) {
            score += 5;
        }
        
        return score;
    }

    private List<SuggestedVendorDTO> sortSuggestedVendors(List<SuggestedVendorDTO> vendors, String sortBy) {
        if (sortBy == null || vendors.isEmpty()) {
            return vendors;
        }
        
        switch (sortBy.toLowerCase()) {
            case "rating":
                return vendors.stream()
                    .sorted((v1, v2) -> {
                        Double rating1 = v1.getAverageRating() != null ? v1.getAverageRating() : 0.0;
                        Double rating2 = v2.getAverageRating() != null ? v2.getAverageRating() : 0.0;
                        return Double.compare(rating2, rating1); // Descending
                    })
                    .collect(Collectors.toList());
                    
            case "price":
                return vendors.stream()
                    .sorted((v1, v2) -> {
                        Double price1 = v1.getMinServicePrice() != null ? v1.getMinServicePrice() : Double.MAX_VALUE;
                        Double price2 = v2.getMinServicePrice() != null ? v2.getMinServicePrice() : Double.MAX_VALUE;
                        return Double.compare(price1, price2); // Ascending
                    })
                    .collect(Collectors.toList());
                    
            case "location":
                return vendors.stream()
                    .sorted((v1, v2) -> {
                        // Sort by location match quality: same location > same country > nearby > different
                        int priority1 = getLocationPriority(v1.getLocationMatch());
                        int priority2 = getLocationPriority(v2.getLocationMatch());
                        return Integer.compare(priority1, priority2);
                    })
                    .collect(Collectors.toList());
                    
            case "popularity":
                return vendors.stream()
                    .sorted((v1, v2) -> {
                        Integer pop1 = v1.getPopularityScore() != null ? v1.getPopularityScore() : 0;
                        Integer pop2 = v2.getPopularityScore() != null ? v2.getPopularityScore() : 0;
                        return Integer.compare(pop2, pop1); // Descending
                    })
                    .collect(Collectors.toList());
                    
            default:
                return vendors;
        }
    }
    
    private int getLocationPriority(String locationMatch) {
        if (locationMatch == null) return 99;
        
        switch (locationMatch.toLowerCase()) {
            case "same location":
                return 1;
            case "same country":
                return 2;
            case "nearby location":
                return 3;
            case "different location":
                return 4;
            default:
                return 5;
        }
    }
}
