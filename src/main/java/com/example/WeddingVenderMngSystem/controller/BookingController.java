package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.BookingDecisionDto;
import com.example.WeddingVenderMngSystem.dto.BookingRequestDto;
import com.example.WeddingVenderMngSystem.dto.BookingResponseDto;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.service.BookingService;
import com.example.WeddingVenderMngSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;

    /**
     * Customer creates a booking request
     * POST /api/bookings/request
     */
    @PostMapping("/request")
    public ResponseEntity<BookingResponseDto> createBookingRequest(@RequestBody BookingRequestDto requestDto) {
        try {
            Long userId = getCurrentUserId();
            BookingResponseDto response = bookingService.createBookingRequest(userId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get all bookings for the current customer
     * GET /api/bookings/customer
     */
    @GetMapping("/customer")
    public ResponseEntity<List<BookingResponseDto>> getCustomerBookings() {
        try {
            Long userId = getCurrentUserId();
            List<BookingResponseDto> bookings = bookingService.getCustomerBookings(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get all bookings for the current vendor
     * GET /api/bookings/vendor
     */
    @GetMapping("/vendor")
    public ResponseEntity<List<BookingResponseDto>> getVendorBookings() {
        try {
            Long userId = getCurrentUserId();
            List<BookingResponseDto> bookings = bookingService.getVendorBookings(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get pending bookings for the current vendor
     * GET /api/bookings/vendor/pending
     */
    @GetMapping("/vendor/pending")
    public ResponseEntity<List<BookingResponseDto>> getPendingBookingsForVendor() {
        try {
            Long userId = getCurrentUserId();
            List<BookingResponseDto> bookings = bookingService.getPendingBookingsForVendor(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Vendor responds to a booking request (accept/reject)
     * PUT /api/bookings/{bookingId}/respond
     */
    @PutMapping("/{bookingId}/respond")
    public ResponseEntity<BookingResponseDto> respondToBooking(
            @PathVariable Long bookingId,
            @RequestBody BookingDecisionDto decisionDto) {
        try {
            Long userId = getCurrentUserId();
            BookingResponseDto response = bookingService.respondToBooking(userId, bookingId, decisionDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get a specific booking for customer
     * GET /api/bookings/{bookingId}/customer
     */
    @GetMapping("/{bookingId}/customer")
    public ResponseEntity<BookingResponseDto> getBookingForCustomer(@PathVariable Long bookingId) {
        try {
            Long userId = getCurrentUserId();
            BookingResponseDto booking = bookingService.getBookingForCustomer(userId, bookingId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get a specific booking for vendor
     * GET /api/bookings/{bookingId}/vendor
     */
    @GetMapping("/{bookingId}/vendor")
    public ResponseEntity<BookingResponseDto> getBookingForVendor(@PathVariable Long bookingId) {
        try {
            Long userId = getCurrentUserId();
            BookingResponseDto booking = bookingService.getBookingForVendor(userId, bookingId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
