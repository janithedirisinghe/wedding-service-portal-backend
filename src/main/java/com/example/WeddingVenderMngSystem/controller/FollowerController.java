package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.FollowRequest;
import com.example.WeddingVenderMngSystem.dto.FollowerDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Follower;
import com.example.WeddingVenderMngSystem.dto.VendorSummaryDTO;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/followers")
@CrossOrigin(origins = "*")
public class FollowerController {
    
    @Autowired
    private FollowerService followerService;
    
    /**
     * Follow a vendor
     */
    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> followVendor(@RequestBody FollowRequest followRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Follower follower = followerService.followVendor(followRequest.getUserId(), followRequest.getVendorId());
            
            FollowerDTO followerDTO = convertToDTO(follower);
            
            response.put("success", true);
            response.put("message", "Successfully followed vendor");
            response.put("data", followerDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Unfollow a vendor
     */
    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, Object>> unfollowVendor(@RequestBody FollowRequest followRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            followerService.unfollowVendor(followRequest.getUserId(), followRequest.getVendorId());
            
            response.put("success", true);
            response.put("message", "Successfully unfollowed vendor");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all followers of a vendor
     */
    @GetMapping("/vendor/{vendorId}/followers")
    public ResponseEntity<Map<String, Object>> getVendorFollowers(@PathVariable Long vendorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Customer> followers = followerService.getVendorFollowers(vendorId);
            Long followerCount = followerService.getFollowerCount(vendorId);
            
            response.put("success", true);
            response.put("followerCount", followerCount);
            response.put("followers", followers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all vendors followed by a customer
     */
    @GetMapping("/customer/{userId}/following")
    public ResponseEntity<Map<String, Object>> getCustomerFollowing(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Original vendor entities (preserved)
            List<Vendor> following = followerService.getCustomerFollowing(userId);
            // Added enriched summaries (new data)
            List<VendorSummaryDTO> followingSummaries = followerService.getCustomerFollowingSummaries(userId);
            Long followingCount = followerService.getFollowingCount(userId);
            
            response.put("success", true);
            response.put("followingCount", followingCount);
            response.put("following", following); // original list retained
            response.put("followingSummaries", followingSummaries); // new enriched list
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Check if customer is following vendor
     */
    @GetMapping("/check/{userId}/{vendorId}")
    public ResponseEntity<Map<String, Object>> checkFollowing(@PathVariable Long userId, @PathVariable Long vendorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isFollowing = followerService.isFollowing(userId, vendorId);
            
            response.put("success", true);
            response.put("isFollowing", isFollowing);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get follower count for a vendor
     */
    @GetMapping("/vendor/{vendorId}/count")
    public ResponseEntity<Map<String, Object>> getFollowerCount(@PathVariable Long vendorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long count = followerService.getFollowerCount(vendorId);
            
            response.put("success", true);
            response.put("followerCount", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get following count for a customer
     */
    @GetMapping("/customer/{userId}/count")
    public ResponseEntity<Map<String, Object>> getFollowingCount(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long count = followerService.getFollowingCount(userId);
            
            response.put("success", true);
            response.put("followingCount", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get detailed follower information for a vendor
     */
    @GetMapping("/vendor/{vendorId}/details")
    public ResponseEntity<Map<String, Object>> getVendorFollowerDetails(@PathVariable Long vendorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Follower> followers = followerService.getActiveFollowers(vendorId);
            List<FollowerDTO> followerDTOs = followers.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("followers", followerDTOs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get detailed following information for a customer
     */
    @GetMapping("/customer/{userId}/details")
    public ResponseEntity<Map<String, Object>> getCustomerFollowingDetails(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Follower> following = followerService.getActiveFollowing(userId);
            List<FollowerDTO> followingDTOs = following.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("following", followingDTOs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Convert Follower entity to DTO
     */
    private FollowerDTO convertToDTO(Follower follower) {
        String customerName = follower.getCustomer().getFirstName() + " " + follower.getCustomer().getLastName();
        String vendorBusinessName = follower.getVendor().getBusinessName();
        
        return new FollowerDTO(
                follower.getFollowerId(),
                follower.getCustomer().getCustomerId(),
                customerName,
                follower.getVendor().getVenderId(),
                vendorBusinessName,
                follower.getFollowedAt(),
                follower.getIsActive()
        );
    }
}
