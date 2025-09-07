package com.example.WeddingVenderMngSystem.dto;

import lombok.Data;

@Data
public class CreatePostDTO {
    
    private String content;
    private String location;
    private String date;
    private Long userId;  // Only userId is sent from frontend
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
