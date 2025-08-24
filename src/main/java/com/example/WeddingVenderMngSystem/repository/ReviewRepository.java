package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVendor_VenderId(Long vendorId);
    List<Review> findByCustomer_CustomerId(Long customerId);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.vendor.venderId = :vendorId")
    Long countByVendorId(@Param("vendorId") Long vendorId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.vendor.venderId = :vendorId")
    Double averageRatingByVendorId(@Param("vendorId") Long vendorId);
}
