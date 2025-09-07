package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.ReviewDTO;
import com.example.WeddingVenderMngSystem.dto.ReviewWithVendorDTO;
import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import com.example.WeddingVenderMngSystem.entity.Review;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.exception.UnauthorizedVendorAccessException;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.ReviewRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AdminNotificationService adminNotificationService;

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

        // Create admin notification for new review
        String title = "New Review Received";
        String message = "Customer " + customer.getUser().getUsername() + " has reviewed vendor " + vendor.getUser().getUsername() + " with rating: " + savedReview.getRating();
        adminNotificationService.createNotification(
            AdminNotificationType.REVIEW_RECEIVED,
            title,
            message,
            savedReview.getReviewId(),
            NotificationPriority.NORMAL
        );

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

    // Method to get reviews by user ID (fetch vendor ID internally)
    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        // Find vendor by user ID
        Optional<Vendor> vendorByUserId = vendorRepository.findByUser_UserId(userId);
        
        if (vendorByUserId.isEmpty()) {
            throw new UnauthorizedVendorAccessException("No vendor found for user ID: " + userId);
        }
        
        Vendor vendor = vendorByUserId.get();
        Long vendorId = vendor.getVenderId();
        
        // Fetch and return the reviews for this vendor
        List<Review> reviews = reviewRepository.findByVendor_VenderId(vendorId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByVendorId(Long vendorId, Long userId) {
        // First, check if the user ID belongs to the vendor ID
        Optional<Vendor> vendorByUserId = vendorRepository.findByUser_UserId(userId);
        
        if (vendorByUserId.isEmpty()) {
            throw new UnauthorizedVendorAccessException("No vendor found for user ID: " + userId);
        }
        
        Vendor vendor = vendorByUserId.get();
        
        // Check if the vendor ID matches the user's vendor ID
        if (!vendor.getVenderId().equals(vendorId)) {
            throw new UnauthorizedVendorAccessException("User ID " + userId + " does not belong to vendor ID " + vendorId);
        }
        
        // If validation passes, fetch and return the reviews
        List<Review> reviews = reviewRepository.findByVendor_VenderId(vendorId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Overloaded method for backward compatibility (without user validation)
    public List<ReviewDTO> getReviewsByVendorId(Long vendorId) {
        List<Review> reviews = reviewRepository.findByVendor_VenderId(vendorId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Method to get reviews by customer user ID
    public List<ReviewDTO> getReviewsByCustomerUserId(Long userId) {
        // Find customer by user ID
        Optional<Customer> customerOpt = customerRepository.findByUser_UserId(userId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found for user ID: " + userId);
        }
        Customer customer = customerOpt.get();
        Long customerId = customer.getCustomerId();
        
        // Fetch and return the reviews for this customer
        List<Review> reviews = reviewRepository.findByCustomer_CustomerId(customerId);
        return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Method to get reviews by customer user ID with vendor details
    public List<ReviewWithVendorDTO> getReviewsByCustomerUserIdWithVendorDetails(Long userId) {
        // Find customer by user ID
        Optional<Customer> customerOpt = customerRepository.findByUser_UserId(userId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found for user ID: " + userId);
        }
        Customer customer = customerOpt.get();
        Long customerId = customer.getCustomerId();
        
        // Fetch and return the reviews for this customer
        List<Review> reviews = reviewRepository.findByCustomer_CustomerId(customerId);
        return reviews.stream().map(this::convertToReviewWithVendorDTO).collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        
        // Set customer name instead of customer ID
        if (review.getCustomer() != null) {
            String firstName = review.getCustomer().getFirstName();
            String lastName = review.getCustomer().getLastName();
            String fullName = "";
            
            if (firstName != null && lastName != null) {
                fullName = firstName + " " + lastName;
            } else if (firstName != null) {
                fullName = firstName;
            } else if (lastName != null) {
                fullName = lastName;
            }
            
            dto.setCustomerName(fullName.trim());
        }
        
        dto.setVendorId(review.getVendor() != null ? review.getVendor().getVenderId() : null);
        return dto;
    }

    private ReviewWithVendorDTO convertToReviewWithVendorDTO(Review review) {
        ReviewWithVendorDTO dto = new ReviewWithVendorDTO();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        
        // Set customer name
        if (review.getCustomer() != null) {
            String firstName = review.getCustomer().getFirstName();
            String lastName = review.getCustomer().getLastName();
            String fullName = "";
            
            if (firstName != null && lastName != null) {
                fullName = firstName + " " + lastName;
            } else if (firstName != null) {
                fullName = firstName;
            } else if (lastName != null) {
                fullName = lastName;
            }
            
            dto.setCustomerName(fullName.trim());
        }
        
        // Set vendor details
        if (review.getVendor() != null) {
            dto.setVendorId(review.getVendor().getVenderId());
            dto.setVendorName(review.getVendor().getBusinessName());
            dto.setVendorType(review.getVendor().getVenType());
        }
        
        return dto;
    }
}
