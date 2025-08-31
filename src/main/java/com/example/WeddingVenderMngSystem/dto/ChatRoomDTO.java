package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private Long chatRoomId;
    private String roomName;
    private Long customerId;
    private String customerName;
    private Long vendorId;
    private String vendorName;
    private String vendorBusinessName;
    private String vendorProfileImageUrl;
    private String customerProfileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
    private ChatRoom.ChatStatus status;
    private List<ChatMessageDTO> recentMessages; // Last few messages for preview
    private int unreadCount;

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public ChatRoom.ChatStatus getStatus() {
        return status;
    }

    public void setStatus(ChatRoom.ChatStatus status) {
        this.status = status;
    }

    public List<ChatMessageDTO> getRecentMessages() {
        return recentMessages;
    }

    public void setRecentMessages(List<ChatMessageDTO> recentMessages) {
        this.recentMessages = recentMessages;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorBusinessName() {
        return vendorBusinessName;
    }

    public void setVendorBusinessName(String vendorBusinessName) {
        this.vendorBusinessName = vendorBusinessName;
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

    public String getVendorProfileImageUrl() {
        return vendorProfileImageUrl;
    }

    public void setVendorProfileImageUrl(String vendorProfileImageUrl) {
        this.vendorProfileImageUrl = vendorProfileImageUrl;
    }

    public String getCustomerProfileImageUrl() {
        return customerProfileImageUrl;
    }

    public void setCustomerProfileImageUrl(String customerProfileImageUrl) {
        this.customerProfileImageUrl = customerProfileImageUrl;
    }
}
