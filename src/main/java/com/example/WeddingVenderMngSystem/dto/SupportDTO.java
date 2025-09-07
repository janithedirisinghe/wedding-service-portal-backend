package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.Support;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SupportDTO {
    private Long supportId;
    private Long userId;
    private String userName;
    private String userRole;
    private String topic;
    private String description;
    private String replyMessage;
    private Support.Severity severity;
    private LocalDateTime createdDate;
    private LocalDateTime replyDate;

    // Getters and Setters
    public Long getSupportId() {
        return supportId;
    }

    public void setSupportId(Long supportId) {
        this.supportId = supportId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Support.Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Support.Severity severity) {
        this.severity = severity;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }
}
