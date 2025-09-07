package com.example.WeddingVenderMngSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorListDTO {
    private Long venderId;
    private String businessName;
    private String profileImageUrl;
    private String venType;
    private Boolean isActive;
    private Boolean verify;

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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getVenType() {
        return venType;
    }

    public void setVenType(String venType) {
        this.venType = venType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }
}
