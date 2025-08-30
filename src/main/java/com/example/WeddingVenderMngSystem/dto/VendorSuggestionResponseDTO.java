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
public class VendorSuggestionResponseDTO {
    private List<SuggestedVendorDTO> suggestedVendors;
    private Integer totalSuggestions;
    private String message;
    private List<String> appliedFilters;

    public List<SuggestedVendorDTO> getSuggestedVendors() {
        return suggestedVendors;
    }

    public void setSuggestedVendors(List<SuggestedVendorDTO> suggestedVendors) {
        this.suggestedVendors = suggestedVendors;
    }

    public Integer getTotalSuggestions() {
        return totalSuggestions;
    }

    public void setTotalSuggestions(Integer totalSuggestions) {
        this.totalSuggestions = totalSuggestions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getAppliedFilters() {
        return appliedFilters;
    }

    public void setAppliedFilters(List<String> appliedFilters) {
        this.appliedFilters = appliedFilters;
    }
}
