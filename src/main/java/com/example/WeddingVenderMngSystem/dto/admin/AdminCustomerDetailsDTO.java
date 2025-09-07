package com.example.WeddingVenderMngSystem.dto.admin;

public class AdminCustomerDetailsDTO {
    private Long customerId;
    private Boolean isActive;
    // Personal Information
    private String firstName;
    private String lastName;
    private java.util.Date dateOfBirth;
    private String phoneNumber;
    private String bio;
    // Address Information
    private String address;
    private String city;
    private String country;
    private String location;
    // Wedding Information
    private java.util.Date weddingDate;
    private String budget;
    // Profile Image
    private String profileImageUrl;
    // Vendor Preferences
    private java.util.List<String> preferredVendorTypes;
    // User Info
    private Long userId;
    private String userName;
    private String userEmail;
    // Derived
    private Integer followerCount;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public java.util.Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(java.util.Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public java.util.Date getWeddingDate() { return weddingDate; }
    public void setWeddingDate(java.util.Date weddingDate) { this.weddingDate = weddingDate; }
    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public java.util.List<String> getPreferredVendorTypes() { return preferredVendorTypes; }
    public void setPreferredVendorTypes(java.util.List<String> preferredVendorTypes) { this.preferredVendorTypes = preferredVendorTypes; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public Integer getFollowerCount() { return followerCount; }
    public void setFollowerCount(Integer followerCount) { this.followerCount = followerCount; }
}
