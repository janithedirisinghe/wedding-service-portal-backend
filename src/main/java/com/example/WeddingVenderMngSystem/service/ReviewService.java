package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.ReviewDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Review;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.ReviewRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).map(this::convertToDTO).orElse(null);
    }
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(reviewDTO.getCreatedAt());

        // Find customer by user ID (sent as customerId from frontend)
        Customer customer = customerRepository.findByUser_UserId(reviewDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + reviewDTO.getCustomerId()));
        
        Vendor vendor = vendorRepository.findById(reviewDTO.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + reviewDTO.getVendorId()));

        review.setCustomer(customer);
        review.setVendor(vendor);

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        return reviewRepository.findById(reviewId).map(review -> {
            review.setRating(reviewDTO.getRating());
            review.setComment(reviewDTO.getComment());

            // Find customer by user ID (sent as customerId from frontend)
            Customer customer = customerRepository.findByUser_UserId(reviewDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + reviewDTO.getCustomerId()));
            
            Vendor vendor = vendorRepository.findById(reviewDTO.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + reviewDTO.getVendorId()));

            review.setCustomer(customer);
            review.setVendor(vendor);

            Review updatedReview = reviewRepository.save(review);
            return convertToDTO(updatedReview);
        }).orElse(null);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewDTO> getReviewsByVendorId(Long vendorId) {
        List<Review> reviews = reviewRepository.findByVendor_VenderId(vendorId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCustomerId(review.getCustomer() != null ? review.getCustomer().getCustomerId() : null);
        dto.setVendorId(review.getVendor() != null ? review.getVendor().getVenderId() : null);
        return dto;
    }
}
