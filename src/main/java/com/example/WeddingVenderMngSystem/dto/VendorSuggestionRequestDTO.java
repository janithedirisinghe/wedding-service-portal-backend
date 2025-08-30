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
public class VendorSuggestionRequestDTO {
    private Long customerId;
    private String customerLocation;
    private Double budget;
    private List<String> preferredVendorTypes;
    private Integer minRating; // minimum average rating (1-5)
    private String sortBy; // "rating", "price", "popularity", "location"
    private Integer limit; // number of suggestions to return (default 10)

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public List<String> getPreferredVendorTypes() {
        return preferredVendorTypes;
    }

    public void setPreferredVendorTypes(List<String> preferredVendorTypes) {
        this.preferredVendorTypes = preferredVendorTypes;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
