package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.AdminNotificationResponseDTO;
import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import com.example.WeddingVenderMngSystem.service.AdminNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
@CrossOrigin(origins = "*")
public class AdminNotificationController {

    @Autowired
    private AdminNotificationService adminNotificationService;

    /**
     * Get all admin notifications with pagination
     */
    @GetMapping
    public ResponseEntity<Page<AdminNotificationResponseDTO>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminNotificationResponseDTO> notifications = adminNotificationService.getAllNotifications(page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread admin notifications
     */
    @GetMapping("/unread")
    public ResponseEntity<Page<AdminNotificationResponseDTO>> getUnreadNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminNotificationResponseDTO> notifications = adminNotificationService.getUnreadNotifications(page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notification count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        long count = adminNotificationService.getUnreadCount();
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Mark a notification as read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<AdminNotificationResponseDTO> markAsRead(@PathVariable Long notificationId) {
        AdminNotificationResponseDTO notification = adminNotificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    /**
     * Mark all notifications as read
     */
    @PutMapping("/mark-all-read")
    public ResponseEntity<Map<String, Object>> markAllAsRead() {
        int updatedCount = adminNotificationService.markAllAsRead();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All notifications marked as read");
        response.put("updatedCount", updatedCount);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a notification
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable Long notificationId) {
        adminNotificationService.deleteNotification(notificationId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Get notifications by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<AdminNotificationResponseDTO>> getNotificationsByType(
            @PathVariable AdminNotificationType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AdminNotificationResponseDTO> notifications = adminNotificationService.getNotificationsByType(type, page, size);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get available notification types
     */
    @GetMapping("/types")
    public ResponseEntity<AdminNotificationType[]> getNotificationTypes() {
        return ResponseEntity.ok(AdminNotificationType.values());
    }
}
