package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDecisionDto {
    private boolean accepted;
    private String vendorNotes;
    
    // Constructors
    public BookingDecisionDto() {}
    
    public BookingDecisionDto(boolean accepted, String vendorNotes) {
        this.accepted = accepted;
        this.vendorNotes = vendorNotes;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getVendorNotes() {
        return vendorNotes;
    }

    public void setVendorNotes(String vendorNotes) {
        this.vendorNotes = vendorNotes;
    }
}
