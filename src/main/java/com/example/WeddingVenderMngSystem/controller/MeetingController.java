package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.MeetingDTO;
import com.example.WeddingVenderMngSystem.dto.MeetingRequestDTO;
import com.example.WeddingVenderMngSystem.dto.MeetingResponseDTO;
import com.example.WeddingVenderMngSystem.entity.Meeting;
import com.example.WeddingVenderMngSystem.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "*")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    /**
     * Customer creates a new meeting request
     */
    @PostMapping("/request/{userId}")
    public ResponseEntity<?> createMeetingRequest(
            @PathVariable Long userId,
            @Valid @RequestBody MeetingRequestDTO meetingRequestDTO) {
        try {
            MeetingDTO meeting = meetingService.createMeetingRequest(userId, meetingRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(meeting);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create meeting request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Vendor responds to a meeting request
     */
    @PutMapping("/respond/{userId}")
    public ResponseEntity<?> respondToMeetingRequest(
            @PathVariable Long userId,
            @Valid @RequestBody MeetingResponseDTO responseDTO) {
        try {
            MeetingDTO meeting = meetingService.respondToMeetingRequest(userId, responseDTO);
            return ResponseEntity.ok(meeting);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to respond to meeting request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all meetings for the current user (customer or vendor)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMeetings(@PathVariable Long userId) {
        try {
            // Try to get customer meetings first
            List<MeetingDTO> customerMeetings = meetingService.getCustomerMeetings(userId);
            if (!customerMeetings.isEmpty()) {
                return ResponseEntity.ok(customerMeetings);
            }
            
            // If no customer meetings found, try vendor meetings
            List<MeetingDTO> vendorMeetings = meetingService.getVendorMeetings(userId);
            return ResponseEntity.ok(vendorMeetings);
        } catch (IllegalArgumentException e) {
            // Try vendor meetings if customer not found
            try {
                List<MeetingDTO> vendorMeetings = meetingService.getVendorMeetings(userId);
                return ResponseEntity.ok(vendorMeetings);
            } catch (IllegalArgumentException e2) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "User not found or not authorized");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get customer meetings
     */
    @GetMapping("/customer/{userId}")
    public ResponseEntity<?> getCustomerMeetings(@PathVariable Long userId) {
        try {
            List<MeetingDTO> meetings = meetingService.getCustomerMeetings(userId);
            return ResponseEntity.ok(meetings);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve customer meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get vendor meetings
     */
    @GetMapping("/vendor/{userId}")
    public ResponseEntity<?> getVendorMeetings(@PathVariable Long userId) {
        try {
            List<MeetingDTO> meetings = meetingService.getVendorMeetings(userId);
            return ResponseEntity.ok(meetings);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve vendor meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get meetings by status for customer
     */
    @GetMapping("/customer/{userId}/status/{status}")
    public ResponseEntity<?> getCustomerMeetingsByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        try {
            Meeting.MeetingStatus meetingStatus = Meeting.MeetingStatus.valueOf(status.toUpperCase());
            List<MeetingDTO> meetings = meetingService.getCustomerMeetingsByStatus(userId, meetingStatus);
            return ResponseEntity.ok(meetings);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid status or user not found: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get meetings by status for vendor
     */
    @GetMapping("/vendor/{userId}/status/{status}")
    public ResponseEntity<?> getVendorMeetingsByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        try {
            Meeting.MeetingStatus meetingStatus = Meeting.MeetingStatus.valueOf(status.toUpperCase());
            List<MeetingDTO> meetings = meetingService.getVendorMeetingsByStatus(userId, meetingStatus);
            return ResponseEntity.ok(meetings);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid status or user not found: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get upcoming meetings for customer
     */
    @GetMapping("/customer/{userId}/upcoming")
    public ResponseEntity<?> getUpcomingCustomerMeetings(@PathVariable Long userId) {
        try {
            List<MeetingDTO> meetings = meetingService.getUpcomingCustomerMeetings(userId);
            return ResponseEntity.ok(meetings);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve upcoming meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get upcoming meetings for vendor
     */
    @GetMapping("/vendor/{userId}/upcoming")
    public ResponseEntity<?> getUpcomingVendorMeetings(@PathVariable Long userId) {
        try {
            List<MeetingDTO> meetings = meetingService.getUpcomingVendorMeetings(userId);
            return ResponseEntity.ok(meetings);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve upcoming meetings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get specific meeting by ID
     */
    @GetMapping("/{meetingId}/user/{userId}")
    public ResponseEntity<?> getMeetingById(
            @PathVariable Long meetingId,
            @PathVariable Long userId) {
        try {
            MeetingDTO meeting = meetingService.getMeetingById(userId, meetingId);
            return ResponseEntity.ok(meeting);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve meeting: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Cancel a meeting
     */
    @PutMapping("/{meetingId}/cancel/{userId}")
    public ResponseEntity<?> cancelMeeting(
            @PathVariable Long meetingId,
            @PathVariable Long userId) {
        try {
            MeetingDTO meeting = meetingService.cancelMeeting(userId, meetingId);
            return ResponseEntity.ok(meeting);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to cancel meeting: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Mark meeting as completed (vendor only)
     */
    @PutMapping("/{meetingId}/complete/{userId}")
    public ResponseEntity<?> completeMeeting(
            @PathVariable Long meetingId,
            @PathVariable Long userId) {
        try {
            MeetingDTO meeting = meetingService.completeMeeting(userId, meetingId);
            return ResponseEntity.ok(meeting);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to complete meeting: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get all possible meeting statuses
     */
    @GetMapping("/statuses")
    public ResponseEntity<Meeting.MeetingStatus[]> getMeetingStatuses() {
        return ResponseEntity.ok(Meeting.MeetingStatus.values());
    }

    /**
     * Get all possible meeting moods
     */
    @GetMapping("/moods")
    public ResponseEntity<Meeting.MeetingMood[]> getMeetingMoods() {
        return ResponseEntity.ok(Meeting.MeetingMood.values());
    }
}
