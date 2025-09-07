package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VendorTypeDTO {
    
    private Long vendorTypeId;
    private String vendorTypeName;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isActive;
    
    // Default constructor
    public VendorTypeDTO() {}
    
    // Constructor with vendor type name only
    public VendorTypeDTO(String vendorTypeName) {
        this.vendorTypeName = vendorTypeName;
        this.isActive = true;
    }
    
    // Constructor with vendor type name and description
    public VendorTypeDTO(String vendorTypeName, String description) {
        this.vendorTypeName = vendorTypeName;
        this.description = description;
        this.isActive = true;
    }
    
    // Constructor with all fields
    public VendorTypeDTO(Long vendorTypeId, String vendorTypeName, String description, 
                        LocalDateTime createdDate, LocalDateTime updatedDate, Boolean isActive) {
        this.vendorTypeId = vendorTypeId;
        this.vendorTypeName = vendorTypeName;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public Long getVendorTypeId() {
        return vendorTypeId;
    }
    
    public void setVendorTypeId(Long vendorTypeId) {
        this.vendorTypeId = vendorTypeId;
    }
    
    public String getVendorTypeName() {
        return vendorTypeName;
    }
    
    public void setVendorTypeName(String vendorTypeName) {
        this.vendorTypeName = vendorTypeName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "VendorTypeDTO{" +
                "vendorTypeId=" + vendorTypeId +
                ", vendorTypeName='" + vendorTypeName + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", isActive=" + isActive +
                '}';
    }
}
