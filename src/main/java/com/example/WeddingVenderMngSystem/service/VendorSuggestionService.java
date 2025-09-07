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
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MeetingRepository meetingRepository;

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

        // Add types from customer preferences (using the simpler approach)
        if (customer != null && customer.getPreferredVendorTypes() != null) {
            types.addAll(customer.getPreferredVendorTypes());
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

    /**
     * Get personalized vendor suggestions based on customer's profile and preferences
     * Uses customer preferred vendors, budget, location, and vendor performance metrics
     * 
     * @param userId The user's ID
     * @return VendorSuggestionResponseDTO with personalized suggestions
     */
    public VendorSuggestionResponseDTO getPersonalizedVendorSuggestions(Long userId) {
        List<String> appliedFilters = new ArrayList<>();
        
        // Get customer details using userId
        final Customer customer;
        if (userId != null) {
            Optional<Customer> customerOpt = customerRepository.findByUser_UserId(userId);
            if (customerOpt.isPresent()) {
                customer = customerOpt.get();
                appliedFilters.add("Customer profile preferences for user: " + userId);
            } else {
                customer = null;
                appliedFilters.add("No customer profile found for user: " + userId);
            }
        } else {
            customer = null;
        }

        return processPersonalizedSuggestions(customer, appliedFilters);
    }

    private VendorSuggestionResponseDTO processPersonalizedSuggestions(Customer customer, List<String> appliedFilters) {
        
        // Get preferred vendor types from customer preferences
        List<String> preferredTypes = getPreferredVendorTypes(customer, null);
        if (!preferredTypes.isEmpty()) {
            appliedFilters.add("Preferred vendor types: " + String.join(", ", preferredTypes));
        }

        // Get customer location and budget
        String searchLocation = getSearchLocation(customer, null);
        if (searchLocation != null && !searchLocation.isEmpty()) {
            appliedFilters.add("Customer location: " + searchLocation);
        }

        Double budgetMax = getBudgetConstraint(customer, null);
        if (budgetMax != null) {
            appliedFilters.add("Budget constraint: ≤ $" + budgetMax);
        }

        // Find vendors with enhanced scoring
        List<Vendor> vendors = findPersonalizedVendors(preferredTypes, searchLocation, budgetMax);
        
        // Convert to DTOs with enhanced metrics
        List<SuggestedVendorDTO> suggestedVendors = vendors.stream()
            .map(vendor -> convertToPersonalizedVendorDTO(vendor, customer))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        // Sort by personalized relevance score
        suggestedVendors = sortPersonalizedVendors(suggestedVendors);

        // Limit to top 15 suggestions
        int limit = 15;
        if (suggestedVendors.size() > limit) {
            suggestedVendors = suggestedVendors.subList(0, limit);
        }

        // Create response
        VendorSuggestionResponseDTO response = new VendorSuggestionResponseDTO();
        response.setSuggestedVendors(suggestedVendors);
        response.setTotalSuggestions(suggestedVendors.size());
        response.setAppliedFilters(appliedFilters);
        
        if (suggestedVendors.isEmpty()) {
            response.setMessage("No personalized vendor suggestions found. Please update your profile preferences.");
        } else {
            response.setMessage("Found " + suggestedVendors.size() + " personalized vendor suggestions based on your profile.");
        }

        return response;
    }

    private List<Vendor> findPersonalizedVendors(List<String> preferredTypes, String location, Double budgetMax) {
        // Get all active and verified vendors
        List<Vendor> vendors = vendorRepository.findAll().stream()
            .filter(vendor -> vendor.getIsActive() != null && vendor.getIsActive())
            .filter(vendor -> vendor.getVerify() != null && vendor.getVerify())
            .collect(Collectors.toList());

        // Apply filters
        if (preferredTypes != null && !preferredTypes.isEmpty()) {
            vendors = vendors.stream()
                .filter(vendor -> preferredTypes.contains(vendor.getVenType()))
                .collect(Collectors.toList());
        }

        if (location != null && !location.trim().isEmpty()) {
            vendors = vendors.stream()
                .filter(vendor -> vendor.getLocation() != null && 
                        vendor.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
        }

        if (budgetMax != null) {
            vendors = vendors.stream()
                .filter(vendor -> {
                    if (vendor.getServices() == null || vendor.getServices().isEmpty()) {
                        return false;
                    }
                    return vendor.getServices().stream()
                        .anyMatch(service -> service.getPricing() != null && service.getPricing() <= budgetMax);
                })
                .collect(Collectors.toList());
        }

        return vendors;
    }

    private SuggestedVendorDTO convertToPersonalizedVendorDTO(Vendor vendor, Customer customer) {
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

            // Calculate ratings and reviews
            Double avgRating = reviewRepository.averageRatingByVendorId(vendor.getVenderId());
            Long reviewCount = reviewRepository.countByVendorId(vendor.getVenderId());
            dto.setAverageRating(avgRating != null ? Math.round(avgRating * 100.0) / 100.0 : null);
            dto.setReviewCount(reviewCount != null ? reviewCount : 0L);

            // Get booking and meeting counts
            Long bookingCount = bookingRepository.countByVendorId(vendor.getVenderId());
            Long meetingCount = meetingRepository.countByVendorId(vendor.getVenderId());

            // Calculate price range
            List<com.example.WeddingVenderMngSystem.entity.Service> services = vendor.getServices();
            if (services != null && !services.isEmpty()) {
                List<ServiceDTO> serviceDTOs = services.stream()
                    .map(this::convertServiceToDTO)
                    .collect(Collectors.toList());
                dto.setServices(serviceDTOs);

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
            dto.setLocationMatch(determineLocationMatch(customer, vendor, null));

            // Enhanced match reason including booking/meeting metrics
            dto.setMatchReason(determinePersonalizedMatchReason(vendor, customer, bookingCount, meetingCount));

            // Calculate enhanced popularity score
            dto.setPopularityScore(calculateEnhancedPopularityScore(vendor, avgRating, reviewCount, bookingCount, meetingCount));

            return dto;
        } catch (Exception e) {
            System.err.println("Error converting vendor " + vendor.getVenderId() + " to personalized DTO: " + e.getMessage());
            return null;
        }
    }

    private String determinePersonalizedMatchReason(Vendor vendor, Customer customer, Long bookingCount, Long meetingCount) {
        List<String> reasons = new ArrayList<>();
        
        // Check if vendor type matches customer preferences
        if (customer != null && customer.getPreferredVendorTypes() != null) {
            boolean isPreferred = customer.getPreferredVendorTypes().contains(vendor.getVenType());
            if (isPreferred) {
                reasons.add("matches your preferred vendor type");
            }
        }
        
        // Check location match
        String searchLocation = getSearchLocation(customer, null);
        if (searchLocation != null && vendor.getLocation() != null &&
            isLocationMatch(searchLocation, vendor.getLocation())) {
            reasons.add("in your preferred location");
        }
        
        // Check rating
        Double avgRating = reviewRepository.averageRatingByVendorId(vendor.getVenderId());
        if (avgRating != null && avgRating >= 4.0) {
            reasons.add("highly rated (" + Math.round(avgRating * 10.0) / 10.0 + " stars)");
        }
        
        // Check booking success rate
        if (bookingCount != null && bookingCount > 0) {
            Long completedBookings = bookingRepository.countCompletedBookingsByVendorId(vendor.getVenderId());
            if (completedBookings != null && completedBookings > 0) {
                double successRate = (double) completedBookings / bookingCount * 100;
                if (successRate >= 80) {
                    reasons.add("high booking success rate (" + Math.round(successRate) + "%)");
                }
            }
        }
        
        // Check meeting engagement
        if (meetingCount != null && meetingCount > 10) {
            reasons.add("experienced with " + meetingCount + " meetings");
        }
        
        // Check budget compatibility
        Double budget = getBudgetConstraint(customer, null);
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

    private Integer calculateEnhancedPopularityScore(Vendor vendor, Double avgRating, Long reviewCount, 
                                                   Long bookingCount, Long meetingCount) {
        int score = 0;
        
        // Rating component (0-40 points)
        if (avgRating != null) {
            score += (int) (avgRating * 8); // 5 stars = 40 points
        }
        
        // Review count component (0-30 points)
        if (reviewCount != null) {
            score += Math.min(30, (int) (reviewCount * 2)); // Max 30 points for reviews
        }
        
        // Booking count component (0-20 points)
        if (bookingCount != null) {
            score += Math.min(20, (int) (bookingCount * 0.5)); // Max 20 points for bookings
        }
        
        // Meeting count component (0-15 points)
        if (meetingCount != null) {
            score += Math.min(15, (int) (meetingCount * 0.3)); // Max 15 points for meetings
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

    private List<SuggestedVendorDTO> sortPersonalizedVendors(List<SuggestedVendorDTO> vendors) {
        if (vendors.isEmpty()) {
            return vendors;
        }
        
        return vendors.stream()
            .sorted((v1, v2) -> {
                // Primary sort: popularity score (descending)
                int scoreCompare = Integer.compare(
                    v2.getPopularityScore() != null ? v2.getPopularityScore() : 0,
                    v1.getPopularityScore() != null ? v1.getPopularityScore() : 0
                );
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                
                // Secondary sort: rating (descending)
                Double rating1 = v1.getAverageRating() != null ? v1.getAverageRating() : 0.0;
                Double rating2 = v2.getAverageRating() != null ? v2.getAverageRating() : 0.0;
                int ratingCompare = Double.compare(rating2, rating1);
                if (ratingCompare != 0) {
                    return ratingCompare;
                }
                
                // Tertiary sort: review count (descending)
                Long reviews1 = v1.getReviewCount() != null ? v1.getReviewCount() : 0L;
                Long reviews2 = v2.getReviewCount() != null ? v2.getReviewCount() : 0L;
                return Long.compare(reviews2, reviews1);
            })
            .collect(Collectors.toList());
    }
}
