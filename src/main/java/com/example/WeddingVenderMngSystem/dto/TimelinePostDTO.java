package com.example.WeddingVenderMngSystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class TimelinePostDTO {
    private Long postId;
    private String content;
    private String location;
    private String date;
    private List<String> itemUrls;
    
    // Vendor information
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    private String vendorProfileImage;
    private String vendorServiceType;
    
    // Timestamps
    private String createdAt;
    private String updatedAt;
    
    public TimelinePostDTO() {}

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

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

    public List<String> getItemUrls() {
        return itemUrls;
    }

    public void setItemUrls(List<String> itemUrls) {
        this.itemUrls = itemUrls;
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

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorProfileImage() {
        return vendorProfileImage;
    }

    public void setVendorProfileImage(String vendorProfileImage) {
        this.vendorProfileImage = vendorProfileImage;
    }

    public String getVendorServiceType() {
        return vendorServiceType;
    }

    public void setVendorServiceType(String vendorServiceType) {
        this.vendorServiceType = vendorServiceType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TimelinePostDTO(Long postId, String content, String location, String date,
                           Long vendorId, String vendorName, String vendorEmail,
                           String vendorProfileImage, String vendorServiceType) {
        this.postId = postId;
        this.content = content;
        this.location = location;
        this.date = date;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorEmail = vendorEmail;
        this.vendorProfileImage = vendorProfileImage;
        this.vendorServiceType = vendorServiceType;
    }
}
