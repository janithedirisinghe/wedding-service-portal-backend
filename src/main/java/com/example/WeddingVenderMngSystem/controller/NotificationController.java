package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.NotificationCreateDTO;
import com.example.WeddingVenderMngSystem.dto.NotificationResponseDTO;
import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import com.example.WeddingVenderMngSystem.entity.NotificationType;
import com.example.WeddingVenderMngSystem.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Create a new notification (mainly for internal use or admin)
     */
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody NotificationCreateDTO createDTO) {
        NotificationResponseDTO notification = notificationService.createNotification(createDTO);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * Create a test notification for debugging
     */
    @PostMapping("/test/{userId}")
    public ResponseEntity<NotificationResponseDTO> createTestNotification(@PathVariable Long userId) {
        NotificationCreateDTO testDTO = new NotificationCreateDTO();
        testDTO.setUserId(userId);
        testDTO.setType(NotificationType.GENERAL);
        testDTO.setTitle("Test Notification");
        testDTO.setMessage("This is a test notification to verify the system is working correctly.");
        testDTO.setPriority(NotificationPriority.NORMAL);
        
        NotificationResponseDTO notification = notificationService.createNotification(testDTO);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * Get notifications for a user with pagination
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<NotificationResponseDTO>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NotificationResponseDTO> notifications = notificationService.getNotificationsForUser(userId, page, size);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Get unread notifications for a user
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<Page<NotificationResponseDTO>> getUnreadNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NotificationResponseDTO> notifications = notificationService.getUnreadNotifications(userId, page, size);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Get unread notification count for a user
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mark a notification as read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        NotificationResponseDTO notification = notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(notification);
    }
    
    /**
     * Mark all notifications as read for a user
     */
    @PutMapping("/user/{userId}/mark-all-read")
    public ResponseEntity<Map<String, Object>> markAllAsRead(@PathVariable Long userId) {
        int updatedCount = notificationService.markAllAsRead(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All notifications marked as read");
        response.put("updatedCount", updatedCount);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete a notification
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, String>> deleteNotification(
            @PathVariable Long notificationId,
            @RequestParam Long userId) {
        notificationService.deleteNotification(notificationId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get notifications by type for a user
     */
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<Page<NotificationResponseDTO>> getNotificationsByType(
            @PathVariable Long userId,
            @PathVariable NotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<NotificationResponseDTO> notifications = notificationService.getNotificationsByType(userId, type, page, size);
        return ResponseEntity.ok(notifications);
    }
    
    /**
     * Get available notification types
     */
    @GetMapping("/types")
    public ResponseEntity<NotificationType[]> getNotificationTypes() {
        return ResponseEntity.ok(NotificationType.values());
    }
    
    /**
     * Debug endpoint to check raw notification data
     */
    @GetMapping("/debug/user/{userId}")
    public ResponseEntity<Map<String, Object>> debugGetUserNotifications(@PathVariable Long userId) {
        try {
            Page<NotificationResponseDTO> notifications = notificationService.getNotificationsForUser(userId, 0, 5);
            Map<String, Object> debug = new HashMap<>();
            debug.put("totalElements", notifications.getTotalElements());
            debug.put("content", notifications.getContent());
            debug.put("hasContent", !notifications.getContent().isEmpty());
            
            if (!notifications.getContent().isEmpty()) {
                NotificationResponseDTO first = notifications.getContent().get(0);
                Map<String, Object> firstNotification = new HashMap<>();
                firstNotification.put("notificationId", first.getNotificationId());
                firstNotification.put("type", first.getType());
                firstNotification.put("title", first.getTitle());
                firstNotification.put("message", first.getMessage());
                firstNotification.put("isRead", first.getIsRead());
                firstNotification.put("priority", first.getPriority());
                firstNotification.put("relatedEntityId", first.getRelatedEntityId());
                firstNotification.put("createdAt", first.getCreatedAt());
                firstNotification.put("readAt", first.getReadAt());
                debug.put("firstNotificationFields", firstNotification);
            }
            
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("stackTrace", e.getStackTrace());
            return ResponseEntity.ok(error);
        }
    }
}
