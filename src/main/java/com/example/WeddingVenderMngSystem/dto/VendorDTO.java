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
public class VendorDTO {
    private Long vendorId;
    private String businessName;
    private String availability;
    private String location;
    private String BRN;
    private String country;
    private String venType;
    private String bio;
    private String telNo;
    private String userName;

    public Long getVendorId() {
        return vendorId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAvailability() {
        return availability;
    }

    public String getLocation() {
        return location;
    }

    public String getBRN() {
        return BRN;
    }

    public String getCountry() {
        return country;
    }

    public String getVenType() {
        return venType;
    }

    public String getBio() {
        return bio;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBRN(String BRN) {
        this.BRN = BRN;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setVenType(String venType) {
        this.venType = venType;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }

    private String userEmail;

    private List<ServiceDTO> services;

    // No-argument constructor (required for some frameworks like Jackson for deserialization)
    public VendorDTO() {
    }

    // All-argument constructor (for convenience when creating instances with all data)
    public VendorDTO(Long vendorId, String businessName, String availability, String location, String BRN,
                     String country, String venType, String bio, String telNo, String userName,
                     String userEmail, List<ServiceDTO> services) {
        this.vendorId = vendorId;
        this.businessName = businessName;
        this.availability = availability;
        this.location = location;
        this.BRN = BRN;
        this.country = country;
        this.venType = venType;
        this.bio = bio;
        this.telNo = telNo;
        this.userName = userName;
        this.userEmail = userEmail;
        this.services = services;
    }
}
