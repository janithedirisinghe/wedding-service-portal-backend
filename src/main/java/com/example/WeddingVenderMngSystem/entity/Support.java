package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supports")
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supportId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String topic;
    private String description;
    private String replyMessage;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private LocalDateTime createdDate;
    private LocalDateTime replyDate;

    public enum Severity {
        LOW, MEDIUM, HIGH, BLOCKER
    }

    // Getters and Setters
    public Long getSupportId() {
        return supportId;
    }

    public void setSupportId(Long supportId) {
        this.supportId = supportId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
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
