package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowRequest {
    private Long userId;
    private Long vendorId;
    
    public FollowRequest() {}
    
    public FollowRequest(Long userId, Long vendorId) {
        this.userId = userId;
        this.vendorId = vendorId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
