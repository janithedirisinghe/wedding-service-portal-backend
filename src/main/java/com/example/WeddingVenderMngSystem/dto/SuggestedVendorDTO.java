package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedVendorDTO {
    private Long vendorId;
    private String businessName;
    private String venType;
    private String location;
    private String country;
    private String bio;
    private String telNo;
    private String userEmail;
    private Double averageRating;
    private Long reviewCount;
    private Double minServicePrice;
    private Double maxServicePrice;
    private List<ServiceDTO> services;
    private String matchReason; // why this vendor was suggested
    private Integer popularityScore; // based on followers, reviews, etc.
    private String locationMatch; // how well location matches (e.g., "same city", "same country", "nearby")

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getVenType() {
        return venType;
    }

    public void setVenType(String venType) {
        this.venType = venType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Double getMinServicePrice() {
        return minServicePrice;
    }

    public void setMinServicePrice(Double minServicePrice) {
        this.minServicePrice = minServicePrice;
    }

    public Double getMaxServicePrice() {
        return maxServicePrice;
    }

    public void setMaxServicePrice(Double maxServicePrice) {
        this.maxServicePrice = maxServicePrice;
    }

    public List<ServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }

    public Integer getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(Integer popularityScore) {
        this.popularityScore = popularityScore;
    }

    public String getLocationMatch() {
        return locationMatch;
    }

    public void setLocationMatch(String locationMatch) {
        this.locationMatch = locationMatch;
    }
}
