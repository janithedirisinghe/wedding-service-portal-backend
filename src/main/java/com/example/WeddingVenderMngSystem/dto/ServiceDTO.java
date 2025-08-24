package com.example.WeddingVenderMngSystem.dto;

import lombok.Data;

@Data
public class ServiceDTO {
    private Long serviceId;
    private String name;
    private String description;
    // New: userId supplied on create instead of vendorId
    private Long userId;
    // Extended fields aligning with Service entity
    private String status; // ACTIVE, INACTIVE, DRAFT
    private String pricingModel; // FIXED, PER_HOUR, etc.
    private Double advancePercentage;
    private Double discountPercent;
    private Integer bookBeforeDays;
    private Boolean isAvailable;
    private String serviceAreaType; // LOCAL, NATIONAL, ONLINE, REMOTE
    private String coverImageUrl;
    private String cancellationPolicy;
    private String createdAt; // ISO string representation
    private String updatedAt; // ISO string representation
    private Boolean isDeleted;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricing() {
        return pricing;
    }

    public void setPricing(Double pricing) {
        this.pricing = pricing;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    private Double pricing;
    private Long vendorId; // Vendor ID reference

    // Getter & Setter for userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPricingModel() { return pricingModel; }
    public void setPricingModel(String pricingModel) { this.pricingModel = pricingModel; }

    public Double getAdvancePercentage() { return advancePercentage; }
    public void setAdvancePercentage(Double advancePercentage) { this.advancePercentage = advancePercentage; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public Integer getBookBeforeDays() { return bookBeforeDays; }
    public void setBookBeforeDays(Integer bookBeforeDays) { this.bookBeforeDays = bookBeforeDays; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }

    public String getServiceAreaType() { return serviceAreaType; }
    public void setServiceAreaType(String serviceAreaType) { this.serviceAreaType = serviceAreaType; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getCancellationPolicy() { return cancellationPolicy; }
    public void setCancellationPolicy(String cancellationPolicy) { this.cancellationPolicy = cancellationPolicy; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean deleted) { isDeleted = deleted; }
}
