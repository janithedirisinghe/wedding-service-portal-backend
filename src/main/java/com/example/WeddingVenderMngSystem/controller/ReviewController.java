package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.ReviewDTO;
import com.example.WeddingVenderMngSystem.exception.UnauthorizedVendorAccessException;
import com.example.WeddingVenderMngSystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    @GetMapping("/vendor/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByVendorId(@PathVariable Long userId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.ok(reviews);
        } catch (UnauthorizedVendorAccessException e) {
            return ResponseEntity.status(403).build(); // Forbidden - user doesn't have access to this vendor's reviews
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // New: Get reviews directly by vendorId (no user validation)
    @GetMapping("/vendorId/{venderId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByVendorIdDirect(@PathVariable Long venderId) {
        try {
            List<ReviewDTO> reviews = reviewService.getReviewsByVendorId(venderId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.createReview(reviewDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
