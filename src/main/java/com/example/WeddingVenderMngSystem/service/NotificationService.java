package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.NotificationCreateDTO;
import com.example.WeddingVenderMngSystem.dto.NotificationResponseDTO;
import com.example.WeddingVenderMngSystem.entity.*;
import com.example.WeddingVenderMngSystem.repository.NotificationRepository;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Create a new notification
     */
    @Transactional
    public NotificationResponseDTO createNotification(NotificationCreateDTO createDTO) {
        User user = userRepository.findById(createDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + createDTO.getUserId()));
        
        Notification notification = new Notification(
            user,
            createDTO.getType(),
            createDTO.getTitle(),
            createDTO.getMessage(),
            createDTO.getRelatedEntityId(),
            createDTO.getPriority()
        );
        
        Notification saved = notificationRepository.save(notification);
        return convertToResponseDTO(saved);
    }
    
    /**
     * Get notifications for a user with pagination
     */
    public Page<NotificationResponseDTO> getNotificationsForUser(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        
        return notifications.map(this::convertToResponseDTO);
    }
    
    /**
     * Get unread notifications for a user
     */
    public Page<NotificationResponseDTO> getUnreadNotifications(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user, pageable);
        
        return notifications.map(this::convertToResponseDTO);
    }
    
    /**
     * Get unread notification count for a user
     */
    public long getUnreadCount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        return notificationRepository.countByUserAndIsReadFalse(user);
    }
    
    /**
     * Mark a notification as read
     */
    @Transactional
    public NotificationResponseDTO markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        
        if (!notification.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Notification does not belong to user");
        }
        
        if (!notification.getIsRead()) {
            notification.markAsRead();
            notification = notificationRepository.save(notification);
        }
        
        return convertToResponseDTO(notification);
    }
    
    /**
     * Mark all notifications as read for a user
     */
    @Transactional
    public int markAllAsRead(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        return notificationRepository.markAllAsReadForUser(user, LocalDateTime.now());
    }
    
    /**
     * Delete a notification
     */
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        
        if (!notification.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Notification does not belong to user");
        }
        
        notificationRepository.delete(notification);
    }
    
    /**
     * Get notifications by type for a user
     */
    public Page<NotificationResponseDTO> getNotificationsByType(Long userId, NotificationType type, int page, int size) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUserAndTypeOrderByCreatedAtDesc(user, type, pageable);
        
        return notifications.map(this::convertToResponseDTO);
    }
    
    /**
     * Helper method for booking notifications
     */
    @Transactional
    public void createBookingNotification(Long userId, NotificationType type, String title, String message, Long bookingId) {
        NotificationCreateDTO createDTO = new NotificationCreateDTO(
            userId, type, title, message, bookingId, NotificationPriority.HIGH
        );
        createNotification(createDTO);
    }
    
    /**
     * Helper method for meeting notifications
     */
    @Transactional
    public void createMeetingNotification(Long userId, NotificationType type, String title, String message, Long meetingId) {
        NotificationCreateDTO createDTO = new NotificationCreateDTO(
            userId, type, title, message, meetingId, NotificationPriority.HIGH
        );
        createNotification(createDTO);
    }
    
    /**
     * Clean up old read notifications (utility method for scheduled cleanup)
     */
    @Transactional
    public int cleanupOldNotifications(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        return notificationRepository.deleteOldReadNotifications(cutoffDate);
    }
    
    /**
     * Convert entity to response DTO
     */
    private NotificationResponseDTO convertToResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setNotificationId(notification.getNotificationId());
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
