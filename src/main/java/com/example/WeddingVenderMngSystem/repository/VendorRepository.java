package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByVenderId(Long venderId);
    Optional<Vendor> findByUser_UserId(Long userId);
    
    // Analytics methods
    Long countByVerifyTrue();
    Long countByVerifyFalse();
    Long countByIsActiveTrue();
    Long countByIsActiveFalse();
    
    // Basic Search Methods
    List<Vendor> findByBusinessNameContainingIgnoreCase(String businessName);
    List<Vendor> findByVenTypeIgnoreCase(String venType);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Vendor> findByLocationContainingIgnoreCase(@Param("location") String location);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.Country) = LOWER(:country)")
    List<Vendor> findByCountryIgnoreCase(@Param("country") String country);
    
    List<Vendor> findByAvailabilityIgnoreCase(String availability);
    List<Vendor> findByIsActiveTrue();
    List<Vendor> findByVerifyTrue();
    List<Vendor> findByIsActiveTrueAndVerifyTrue();
    
    // Advanced Search Methods
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.venType) = LOWER(:venType) AND LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Vendor> findByVenTypeIgnoreCaseAndLocationContainingIgnoreCase(@Param("venType") String venType, @Param("location") String location);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%')) AND LOWER(v.Country) = LOWER(:country)")
    List<Vendor> findByLocationContainingIgnoreCaseAndCountryIgnoreCase(@Param("location") String location, @Param("country") String country);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.venType) = LOWER(:venType) AND LOWER(v.Country) = LOWER(:country)")
    List<Vendor> findByVenTypeIgnoreCaseAndCountryIgnoreCase(@Param("venType") String venType, @Param("country") String country);
    
    // Search with Status Filters
    List<Vendor> findByVenTypeIgnoreCaseAndIsActiveTrueAndVerifyTrue(String venType);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%')) AND v.isActive = true AND v.verify = true")
    List<Vendor> findByLocationContainingIgnoreCaseAndIsActiveTrueAndVerifyTrue(@Param("location") String location);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.Country) = LOWER(:country) AND v.isActive = true AND v.verify = true")
    List<Vendor> findByCountryIgnoreCaseAndIsActiveTrueAndVerifyTrue(@Param("country") String country);
    
    // Bio/Description Search
    List<Vendor> findByBioContainingIgnoreCase(String keyword);
    List<Vendor> findByBioContainingIgnoreCaseAndIsActiveTrueAndVerifyTrue(String keyword);
    
    // Multi-criteria Search
    @Query("SELECT v FROM Vendor v WHERE " +
           "(:businessName IS NULL OR LOWER(v.businessName) LIKE LOWER(CONCAT('%', :businessName, '%'))) AND " +
           "(:venType IS NULL OR LOWER(v.venType) = LOWER(:venType)) AND " +
           "(:location IS NULL OR LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:country IS NULL OR LOWER(v.Country) = LOWER(:country)) AND " +
           "(:availability IS NULL OR LOWER(v.availability) = LOWER(:availability)) AND " +
           "(:isActive IS NULL OR v.isActive = :isActive) AND " +
           "(:verify IS NULL OR v.verify = :verify)")
    List<Vendor> searchVendors(@Param("businessName") String businessName,
                              @Param("venType") String venType,
                              @Param("location") String location,
                              @Param("country") String country,
                              @Param("availability") String availability,
                              @Param("isActive") Boolean isActive,
                              @Param("verify") Boolean verify);
    
    // Paginated Search
    @Query("SELECT v FROM Vendor v WHERE " +
           "(:businessName IS NULL OR LOWER(v.businessName) LIKE LOWER(CONCAT('%', :businessName, '%'))) AND " +
           "(:venType IS NULL OR LOWER(v.venType) = LOWER(:venType)) AND " +
           "(:location IS NULL OR LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:country IS NULL OR LOWER(v.Country) = LOWER(:country)) AND " +
           "(:isActive IS NULL OR v.isActive = :isActive) AND " +
           "(:verify IS NULL OR v.verify = :verify)")
    Page<Vendor> searchVendorsWithPagination(@Param("businessName") String businessName,
                                            @Param("venType") String venType,
                                            @Param("location") String location,
                                            @Param("country") String country,
                                            @Param("isActive") Boolean isActive,
                                            @Param("verify") Boolean verify,
                                            Pageable pageable);
    
    // Service-based Search
    @Query("SELECT DISTINCT v FROM Vendor v JOIN v.services s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :serviceName, '%')) AND " +
           "v.isActive = true AND v.verify = true")
    List<Vendor> findByServiceNameContaining(@Param("serviceName") String serviceName);
    
    @Query("SELECT DISTINCT v FROM Vendor v JOIN v.services s WHERE " +
           "s.pricing BETWEEN :minPrice AND :maxPrice AND " +
           "v.isActive = true AND v.verify = true")
    List<Vendor> findByServicePriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT DISTINCT v FROM Vendor v JOIN v.services s WHERE " +
           "LOWER(s.pricingModel) = LOWER(:pricingModel) AND " +
           "v.isActive = true AND v.verify = true")
    List<Vendor> findByServicePricingModel(@Param("pricingModel") String pricingModel);
    
    // Follower-based Search
    @Query("SELECT v FROM Vendor v WHERE SIZE(v.followers) >= :minFollowers AND " +
           "v.isActive = true AND v.verify = true ORDER BY SIZE(v.followers) DESC")
    List<Vendor> findVendorsWithMinimumFollowers(@Param("minFollowers") int minFollowers);
    
    @Query("SELECT v FROM Vendor v WHERE v.isActive = true AND v.verify = true " +
           "ORDER BY SIZE(v.followers) DESC")
    List<Vendor> findMostFollowedVendors(Pageable pageable);
    
    // Auto-complete Search
    @Query("SELECT DISTINCT v.businessName FROM Vendor v WHERE " +
           "LOWER(v.businessName) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           "v.isActive = true AND v.verify = true")
    List<String> findBusinessNameSuggestions(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT DISTINCT v.venType FROM Vendor v WHERE " +
           "LOWER(v.venType) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           "v.isActive = true AND v.verify = true")
    List<String> findVenTypeSuggestions(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT DISTINCT v.Location FROM Vendor v WHERE " +
           "LOWER(v.Location) LIKE LOWER(CONCAT('%', :query, '%')) AND " +
           "v.isActive = true AND v.verify = true")
    List<String> findLocationSuggestions(@Param("query") String query, Pageable pageable);
    
    // Vendor Suggestion Methods
    @Query("SELECT DISTINCT v FROM Vendor v LEFT JOIN v.services s WHERE " +
           "v.isActive = true AND v.verify = true AND " +
           "(:venTypes IS NULL OR v.venType IN :venTypes) AND " +
           "(:location IS NULL OR LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:country IS NULL OR LOWER(v.Country) = LOWER(:country)) AND " +
           "(:minPrice IS NULL OR s.pricing >= :minPrice) AND " +
           "(:maxPrice IS NULL OR s.pricing <= :maxPrice)")
    List<Vendor> findSuggestedVendors(@Param("venTypes") List<String> venTypes,
                                     @Param("location") String location,
                                     @Param("country") String country,
                                     @Param("minPrice") Double minPrice,
                                     @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT DISTINCT v FROM Vendor v LEFT JOIN v.services s WHERE " +
           "v.isActive = true AND v.verify = true AND " +
           "(:venTypes IS NULL OR v.venType IN :venTypes) AND " +
           "(:location IS NULL OR LOWER(v.Location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:country IS NULL OR LOWER(v.Country) = LOWER(:country)) AND " +
           "(:minPrice IS NULL OR s.pricing >= :minPrice) AND " +
           "(:maxPrice IS NULL OR s.pricing <= :maxPrice) " +
           "ORDER BY v.venderId")
    Page<Vendor> findSuggestedVendorsWithPagination(@Param("venTypes") List<String> venTypes,
                                                   @Param("location") String location,
                                                   @Param("country") String country,
                                                   @Param("minPrice") Double minPrice,
                                                   @Param("maxPrice") Double maxPrice,
                                                   Pageable pageable);
}
