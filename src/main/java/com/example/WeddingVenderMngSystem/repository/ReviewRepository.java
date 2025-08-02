package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVendor_VenderId(Long vendorId);
    List<Review> findByCustomer_CustomerId(Long customerId);
}
