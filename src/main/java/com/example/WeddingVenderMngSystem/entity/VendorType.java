package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_types")
@Getter
@Setter
public class VendorType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorTypeId;
    
    @Column(name = "vendor_type_name", nullable = false, unique = true)
    private String vendorTypeName;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;
    
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;
    
    // Default constructor
    public VendorType() {}
    
    // Constructor with vendor type name
    public VendorType(String vendorTypeName) {
        this.vendorTypeName = vendorTypeName;
    }
    
    // Constructor with vendor type name and description
    public VendorType(String vendorTypeName, String description) {
        this.vendorTypeName = vendorTypeName;
        this.description = description;
    }
    
    // Constructor with all fields except ID and timestamps
    public VendorType(String vendorTypeName, String description, Boolean isActive) {
        this.vendorTypeName = vendorTypeName;
        this.description = description;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        VendorType that = (VendorType) o;
        
        if (vendorTypeId != null ? !vendorTypeId.equals(that.vendorTypeId) : that.vendorTypeId != null)
            return false;
        return vendorTypeName != null ? vendorTypeName.equals(that.vendorTypeName) : that.vendorTypeName == null;
    }
    
    @Override
    public int hashCode() {
        int result = vendorTypeId != null ? vendorTypeId.hashCode() : 0;
        result = 31 * result + (vendorTypeName != null ? vendorTypeName.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "VendorType{" +
                "vendorTypeId=" + vendorTypeId +
                ", vendorTypeName='" + vendorTypeName + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", isActive=" + isActive +
                '}';
    }
}
