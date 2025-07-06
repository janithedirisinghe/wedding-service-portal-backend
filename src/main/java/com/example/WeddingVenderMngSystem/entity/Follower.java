package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "followers")
@Getter
@Setter
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "followed_at", nullable = false)
    private LocalDateTime followedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Constructors
    public Follower() {
        this.followedAt = LocalDateTime.now();
        this.isActive = true;
    }

    public Follower(Customer customer, Vendor vendor) {
        this.customer = customer;
        this.vendor = vendor;
        this.followedAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Getters and Setters
    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public LocalDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(LocalDateTime followedAt) {
        this.followedAt = followedAt;
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
        if (!(o instanceof Follower)) return false;
        
        Follower follower = (Follower) o;
        
        if (customer != null ? !customer.getCustomerId().equals(follower.customer.getCustomerId()) : follower.customer != null)
            return false;
        return vendor != null ? vendor.getVenderId().equals(follower.vendor.getVenderId()) : follower.vendor == null;
    }

    @Override
    public int hashCode() {
        int result = customer != null ? customer.getCustomerId().hashCode() : 0;
        result = 31 * result + (vendor != null ? vendor.getVenderId().hashCode() : 0);
        return result;
    }
}
