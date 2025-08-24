package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    private String name;
    private String description;
    private Double pricing;

    // New fields (defaults applied in Java; optional DB defaults via columnDefinition)
    @Column(name = "status", nullable = false)
    private String status = "ACTIVE"; // e.g. ACTIVE, INACTIVE, DRAFT

    @Column(name = "pricing_model", nullable = false)
    private String pricingModel = "FIXED"; // FIXED, PER_HOUR, PER_PERSON, PACKAGE

    @Column(name = "advance_percentage", nullable = false)
    private Double advancePercentage = 0.0; // Advance payment percentage

    @Column(name = "discount_percent", nullable = false)
    private Double discountPercent = 0.0; // Promotional discount percentage

    @Column(name = "book_before_days", nullable = false)
    private Integer bookBeforeDays = 0; // Min days in advance a booking must be made

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true; // Cached availability flag

    @Column(name = "service_area_type", nullable = false)
    private String serviceAreaType = "LOCAL"; // LOCAL, NATIONAL, ONLINE, REMOTE

    @Column(name = "cover_image_url")
    private String coverImageUrl; // Optional main image

    @Column(name = "cancellation_policy")
    private String cancellationPolicy = "STANDARD"; // Policy code / simple text

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false; // Soft delete flag

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    @JsonBackReference
    private Vendor vendor;

    public Long getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPricing() {
        return pricing;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPricing(Double pricing) {
        this.pricing = pricing;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    // Getters & Setters for new fields
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean deleted) { isDeleted = deleted; }
}
