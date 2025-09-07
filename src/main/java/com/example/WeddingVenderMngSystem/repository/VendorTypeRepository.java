package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.VendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorTypeRepository extends JpaRepository<VendorType, Long> {
    
    /**
     * Find vendor type by name
     */
    Optional<VendorType> findByVendorTypeName(String vendorTypeName);
    
    /**
     * Find vendor type by name (case insensitive)
     */
    @Query("SELECT vt FROM VendorType vt WHERE LOWER(vt.vendorTypeName) = LOWER(:vendorTypeName)")
    Optional<VendorType> findByVendorTypeNameIgnoreCase(@Param("vendorTypeName") String vendorTypeName);
    
    /**
     * Find all active vendor types
     */
    List<VendorType> findByIsActiveTrue();
    
    /**
     * Find all inactive vendor types
     */
    List<VendorType> findByIsActiveFalse();
    
    /**
     * Check if vendor type exists by name
     */
    boolean existsByVendorTypeName(String vendorTypeName);
    
    /**
     * Check if vendor type exists by name (case insensitive)
     */
    @Query("SELECT COUNT(vt) > 0 FROM VendorType vt WHERE LOWER(vt.vendorTypeName) = LOWER(:vendorTypeName)")
    boolean existsByVendorTypeNameIgnoreCase(@Param("vendorTypeName") String vendorTypeName);
    
    /**
     * Find vendor types containing name (case insensitive)
     */
    @Query("SELECT vt FROM VendorType vt WHERE LOWER(vt.vendorTypeName) LIKE LOWER(CONCAT('%', :vendorTypeName, '%'))")
    List<VendorType> findByVendorTypeNameContainingIgnoreCase(@Param("vendorTypeName") String vendorTypeName);
    
    /**
     * Find active vendor types containing name (case insensitive)
     */
    @Query("SELECT vt FROM VendorType vt WHERE LOWER(vt.vendorTypeName) LIKE LOWER(CONCAT('%', :vendorTypeName, '%')) AND vt.isActive = true")
    List<VendorType> findByVendorTypeNameContainingIgnoreCaseAndIsActiveTrue(@Param("vendorTypeName") String vendorTypeName);
    
    /**
     * Count active vendor types
     */
    @Query("SELECT COUNT(vt) FROM VendorType vt WHERE vt.isActive = true")
    long countActiveVendorTypes();
    
    /**
     * Count inactive vendor types
     */
    @Query("SELECT COUNT(vt) FROM VendorType vt WHERE vt.isActive = false")
    long countInactiveVendorTypes();
}
