package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;
    
    @Column(unique = true, nullable = false)
    private String roomName; // Format: "customer_{customerId}_vendor_{vendorId}"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime lastMessageAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatStatus status = ChatStatus.ACTIVE;
    
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ChatMessage> messages;
    
    public enum ChatStatus {
        ACTIVE, ARCHIVED, BLOCKED
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastMessageAt = LocalDateTime.now();
    }
    
    public void updateLastMessageTime() {
        lastMessageAt = LocalDateTime.now();
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public ChatStatus getStatus() {
        return status;
    }

    public void setStatus(ChatStatus status) {
        this.status = status;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
