package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminNotificationResponseDTO {
    private Long adminNotificationId;
    private AdminNotificationType type;
    private String title;
    private String message;
    private Boolean isRead;
    private NotificationPriority priority;
    private Long relatedEntityId;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
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
}
