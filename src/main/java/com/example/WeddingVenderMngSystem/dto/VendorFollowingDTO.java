package com.example.WeddingVenderMngSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorFollowingDTO {
    private Long venderId;
    private String businessName;
    private String availability;
    private String bio;

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public List<ServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }

    private String telNo;
    private List<ServiceDTO> services;
    
    // Constructor for easy mapping from Vendor entity
    public VendorFollowingDTO(Long venderId, String businessName, String availability, String bio, String telNo) {
        this.venderId = venderId;
        this.businessName = businessName;
        this.availability = availability;
        this.bio = bio;
        this.telNo = telNo;
    }
}
