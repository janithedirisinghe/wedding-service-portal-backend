package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import com.example.WeddingVenderMngSystem.entity.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationResponseDTO {
    private Long notificationId;
    private NotificationType type;
    private String title;
    private String message;
    private Boolean isRead;
    private NotificationPriority priority;
    private Long relatedEntityId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readAt;
    
    // Constructor
    public NotificationResponseDTO() {}
    
    public NotificationResponseDTO(Long notificationId, NotificationType type, String title, 
                                 String message, Boolean isRead, NotificationPriority priority, 
                                 Long relatedEntityId, LocalDateTime createdAt, LocalDateTime readAt) {
        this.notificationId = notificationId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.priority = priority;
        this.relatedEntityId = relatedEntityId;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
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
}
