package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class FollowerDTO {
    private Long followerId;
    private Long customerId;
    private String customerName;
    private Long vendorId;
    private String vendorBusinessName;
    private LocalDateTime followedAt;
    private Boolean isActive;
    
    public FollowerDTO() {}
    
    public FollowerDTO(Long followerId, Long customerId, String customerName, 
                      Long vendorId, String vendorBusinessName, 
                      LocalDateTime followedAt, Boolean isActive) {
        this.followerId = followerId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.vendorId = vendorId;
        this.vendorBusinessName = vendorBusinessName;
        this.followedAt = followedAt;
        this.isActive = isActive;
    }
    
    public Long getFollowerId() {
        return followerId;
    }
    
    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
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
    
    public String getVendorBusinessName() {
        return vendorBusinessName;
    }
    
    public void setVendorBusinessName(String vendorBusinessName) {
        this.vendorBusinessName = vendorBusinessName;
    }
    
    public LocalDateTime getFollowedAt() {
        return followedAt;
    }
    
    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
