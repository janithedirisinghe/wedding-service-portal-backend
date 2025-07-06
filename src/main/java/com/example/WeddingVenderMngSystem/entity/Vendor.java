package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "venders")
@Getter
@Setter
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venderId;

    private String businessName;
    private String availability;

    private String Location;
    private  String BRN;
    private String Country;
    @JsonProperty("VenType") // Ensures JSON maps correctly
    private String venType;   // Changed from "VenType" to "venType"
    private String bio;
    private String telNo;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Service> services;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    // Follower relationships
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follower> followers;

    public void setUser(User user) {
        this.user = user;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setBRN(String BRN) {
        this.BRN = BRN;
    }

    public void setCountry(String country) {
        Country = country;
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


    public Long getVenderId() {

        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getAvailability() {
        return availability;
    }

    public String getLocation() {
        return Location;
    }

    public String getBRN() {
        return BRN;
    }

    public String getCountry() {
        return Country;
    }

    public String getBio() {
        return bio;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getVenType() { return venType; }



    public List<Service> getServices() {
        return services;
    }

    public User getUser() {
        return user;
    }

    public Admin getAdmin() {
        return admin;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }
}