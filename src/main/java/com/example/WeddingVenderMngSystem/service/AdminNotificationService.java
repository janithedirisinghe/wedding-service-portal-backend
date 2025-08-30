package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.AdminNotificationResponseDTO;
import com.example.WeddingVenderMngSystem.entity.AdminNotification;
import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import com.example.WeddingVenderMngSystem.repository.AdminNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminNotificationService {

    @Autowired
    private AdminNotificationRepository adminNotificationRepository;

    /**
     * Create a new admin notification
     */
    @Transactional
    public AdminNotificationResponseDTO createNotification(AdminNotificationType type, String title, String message, Long relatedEntityId) {
        AdminNotification notification = new AdminNotification(type, title, message, relatedEntityId);
        AdminNotification saved = adminNotificationRepository.save(notification);
        return convertToResponseDTO(saved);
    }

    /**
     * Create a new admin notification with priority
     */
    @Transactional
    public AdminNotificationResponseDTO createNotification(AdminNotificationType type, String title, String message, Long relatedEntityId, NotificationPriority priority) {
        AdminNotification notification = new AdminNotification(type, title, message, relatedEntityId, priority);
        AdminNotification saved = adminNotificationRepository.save(notification);
        return convertToResponseDTO(saved);
    }

    /**
     * Get all admin notifications with pagination
     */
    public Page<AdminNotificationResponseDTO> getAllNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminNotification> notifications = adminNotificationRepository.findAll(pageable);
        return notifications.map(this::convertToResponseDTO);
    }

    /**
     * Get unread admin notifications
     */
    public Page<AdminNotificationResponseDTO> getUnreadNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminNotification> notifications = adminNotificationRepository.findByIsReadFalseOrderByCreatedAtDesc(pageable);
        return notifications.map(this::convertToResponseDTO);
    }

    /**
     * Get unread notification count
     */
    public long getUnreadCount() {
        return adminNotificationRepository.countByIsReadFalse();
    }

    /**
     * Mark a notification as read
     */
    @Transactional
    public AdminNotificationResponseDTO markAsRead(Long notificationId) {
        AdminNotification notification = adminNotificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Admin notification not found with ID: " + notificationId));

        if (!notification.getIsRead()) {
            notification.markAsRead();
            notification = adminNotificationRepository.save(notification);
        }

        return convertToResponseDTO(notification);
    }

    /**
     * Mark all notifications as read
     */
    @Transactional
    public int markAllAsRead() {
        return adminNotificationRepository.markAllAsRead(LocalDateTime.now());
    }

    /**
     * Delete a notification
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        adminNotificationRepository.deleteById(notificationId);
    }

    /**
     * Get notifications by type
     */
    public Page<AdminNotificationResponseDTO> getNotificationsByType(AdminNotificationType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdminNotification> notifications = adminNotificationRepository.findByTypeOrderByCreatedAtDesc(type, pageable);
        return notifications.map(this::convertToResponseDTO);
    }

    /**
     * Clean up old read notifications (utility method for scheduled cleanup)
     */
    @Transactional
    public int cleanupOldNotifications(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        return adminNotificationRepository.deleteOldReadNotifications(cutoffDate);
    }

    /**
     * Convert entity to response DTO
     */
    private AdminNotificationResponseDTO convertToResponseDTO(AdminNotification notification) {
        AdminNotificationResponseDTO dto = new AdminNotificationResponseDTO();
        dto.setAdminNotificationId(notification.getAdminNotificationId());
        dto.setType(notification.getType());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setIsRead(notification.getIsRead());
        dto.setPriority(notification.getPriority());
        dto.setRelatedEntityId(notification.getRelatedEntityId());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReadAt(notification.getReadAt());
        return dto;
    }
}
