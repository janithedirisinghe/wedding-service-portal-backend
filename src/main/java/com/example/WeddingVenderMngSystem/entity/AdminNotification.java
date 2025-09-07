package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_notifications")
@Getter
@Setter
public class AdminNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminNotificationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminNotificationType type;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Boolean isRead = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readAt;

    // Reference to related entity IDs for navigation
    private Long relatedEntityId; // Can be supportId, serviceId, reviewId, paymentId, userId, etc.

    @Enumerated(EnumType.STRING)
    private NotificationPriority priority = NotificationPriority.NORMAL;

    // Constructor
    public AdminNotification() {}

    public AdminNotification(AdminNotificationType type, String title, String message, Long relatedEntityId) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedEntityId = relatedEntityId;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public AdminNotification(AdminNotificationType type, String title, String message, Long relatedEntityId, NotificationPriority priority) {
        this(type, title, message, relatedEntityId);
        this.priority = priority;
    }

    // Helper method to mark as read
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public Long getAdminNotificationId() {
        return adminNotificationId;
    }

    public void setAdminNotificationId(Long adminNotificationId) {
        this.adminNotificationId = adminNotificationId;
    }

    public AdminNotificationType getType() {
        return type;
    }

    public void setType(AdminNotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }
}
