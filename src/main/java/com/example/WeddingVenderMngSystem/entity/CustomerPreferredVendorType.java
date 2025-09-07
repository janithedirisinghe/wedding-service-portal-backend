package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customer_preferred_vendor_types")
@Getter
@Setter
public class CustomerPreferredVendorType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vendor_type", nullable = false)
    private String vendorType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
    
    // Default constructor
    public CustomerPreferredVendorType() {}
    
    // Constructor with vendor type
    public CustomerPreferredVendorType(String vendorType) {
        this.vendorType = vendorType;
    }
    
    // Constructor with vendor type and customer
    public CustomerPreferredVendorType(String vendorType, Customer customer) {
        this.vendorType = vendorType;
        this.customer = customer;
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
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CustomerPreferredVendorType that = (CustomerPreferredVendorType) o;
        
        if (vendorType != null ? !vendorType.equals(that.vendorType) : that.vendorType != null) return false;
        return customer != null ? customer.equals(that.customer) : that.customer == null;
    }
    
    @Override
    public int hashCode() {
        int result = vendorType != null ? vendorType.hashCode() : 0;
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "CustomerPreferredVendorType{" +
                "id=" + id +
                ", vendorType='" + vendorType + '\'' +
                ", customerId=" + (customer != null ? customer.getCustomerId() : null) +
                '}';
    }
}
