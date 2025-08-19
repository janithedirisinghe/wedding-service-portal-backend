package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.PaymentConfirmationDto;
import com.example.WeddingVenderMngSystem.dto.PaymentRequestDto;
import com.example.WeddingVenderMngSystem.dto.PaymentResponseDto;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.service.PaymentService;
import com.example.WeddingVenderMngSystem.service.UserService;
import com.stripe.exception.StripeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private UserService userService;

    /**
     * Create a payment intent for a booking
     * POST /api/payments/create-intent
     */
    @PostMapping("/create-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequestDto paymentRequest) {
        try {
            logger.info("Creating payment intent for booking: {}", paymentRequest.getBookingId());
            
            // Validate input
            if (paymentRequest.getBookingId() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Booking ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Valid amount is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Long userId = getCurrentUserId();
            logger.info("User ID: {}", userId);
            
            PaymentResponseDto response = paymentService.createPaymentIntent(userId, paymentRequest);
            logger.info("Payment intent created successfully: {}", response.getStripePaymentIntentId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (StripeException e) {
            logger.error("Stripe error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment processing error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        } catch (RuntimeException e) {
            logger.error("Business logic error: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Confirm payment after successful payment on frontend
     * POST /api/payments/confirm
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmationDto confirmationDto) {
        try {
            logger.info("Confirming payment for intent: {}", confirmationDto.getStripePaymentIntentId());
            
            // Validate input
            if (confirmationDto.getStripePaymentIntentId() == null || confirmationDto.getStripePaymentIntentId().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Stripe Payment Intent ID is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Long userId = getCurrentUserId();
            PaymentResponseDto response = paymentService.confirmPayment(userId, confirmationDto);
            logger.info("Payment confirmed successfully: {}", response.getStripePaymentIntentId());
            
            return ResponseEntity.ok(response);
            
        } catch (StripeException e) {
            logger.error("Stripe error during confirmation: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Payment confirmation error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        } catch (RuntimeException e) {
            logger.error("Business logic error during confirmation: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            
        } catch (Exception e) {
            logger.error("Unexpected error during confirmation: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get payment by booking ID
     * GET /api/payments/booking/{bookingId}
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBookingId(@PathVariable Long bookingId) {
        try {
            logger.info("Getting payment for booking: {}", bookingId);
            Long userId = getCurrentUserId();
            PaymentResponseDto payment = paymentService.getPaymentByBookingId(userId, bookingId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            logger.error("Error getting payment for booking {}: {}", bookingId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error getting payment for booking {}: {}", bookingId, e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all payments for the current customer
     * GET /api/payments/customer
     */
    @GetMapping("/customer")
    public ResponseEntity<?> getCustomerPayments() {
        try {
            Long userId = getCurrentUserId();
            List<PaymentResponseDto> payments = paymentService.getCustomerPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (RuntimeException e) {
            logger.error("Error getting customer payments: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error getting customer payments: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all payments for the current vendor
     * GET /api/payments/vendor
     */
    @GetMapping("/vendor")
    public ResponseEntity<?> getVendorPayments() {
        try {
            Long userId = getCurrentUserId();
            List<PaymentResponseDto> payments = paymentService.getVendorPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (RuntimeException e) {
            logger.error("Error getting vendor payments: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            logger.error("Unexpected error getting vendor payments: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Test endpoint to check authentication
     * GET /api/payments/test-auth
     */
    @GetMapping("/test-auth")
    public ResponseEntity<?> testAuth() {
        try {
            Long userId = getCurrentUserId();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Authentication successful");
            response.put("userId", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authentication test failed: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * Helper method to get current user ID from security context
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                return user.getUserId();
            }
        }
        throw new RuntimeException("User not authenticated");
    }
}
