package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerPreferredVendorTypeDTO {
    
    private Long id;
    private String vendorType;
    private Long customerId;
    
    // Default constructor
    public CustomerPreferredVendorTypeDTO() {}
    
    // Constructor with vendor type
    public CustomerPreferredVendorTypeDTO(String vendorType) {
        this.vendorType = vendorType;
    }
    
    // Constructor with all fields
    public CustomerPreferredVendorTypeDTO(Long id, String vendorType, Long customerId) {
        this.id = id;
        this.vendorType = vendorType;
        this.customerId = customerId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getVendorType() {
        return vendorType;
    }
    
    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    @Override
    public String toString() {
        return "CustomerPreferredVendorTypeDTO{" +
                "id=" + id +
                ", vendorType='" + vendorType + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}

/**
 * DTO for bulk operations on customer preferred vendor types
 */
@Getter
@Setter
class CustomerPreferredVendorTypesBulkDTO {
    
    private Long customerId;
    private List<String> vendorTypes;
    
    // Default constructor
    public CustomerPreferredVendorTypesBulkDTO() {}
    
    // Constructor
    public CustomerPreferredVendorTypesBulkDTO(Long customerId, List<String> vendorTypes) {
        this.customerId = customerId;
        this.vendorTypes = vendorTypes;
    }
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public List<String> getVendorTypes() {
        return vendorTypes;
    }
    
    public void setVendorTypes(List<String> vendorTypes) {
        this.vendorTypes = vendorTypes;
    }
    
    @Override
    public String toString() {
        return "CustomerPreferredVendorTypesBulkDTO{" +
                "customerId=" + customerId +
                ", vendorTypes=" + vendorTypes +
                '}';
    }
}
