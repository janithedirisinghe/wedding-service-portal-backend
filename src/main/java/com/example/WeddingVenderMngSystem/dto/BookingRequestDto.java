package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BookingRequestDto {
    private Long serviceId;
    private Date eventDate;
    private String eventLocation;
    private String specialRequirements;
    private BigDecimal proposedPrice;
    
    // Constructors
    public BookingRequestDto() {}
    
    public BookingRequestDto(Long serviceId, Date eventDate, String eventLocation, 
                           String specialRequirements, BigDecimal proposedPrice) {
        this.serviceId = serviceId;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.specialRequirements = specialRequirements;
        this.proposedPrice = proposedPrice;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public BigDecimal getProposedPrice() {
        return proposedPrice;
    }

    public void setProposedPrice(BigDecimal proposedPrice) {
        this.proposedPrice = proposedPrice;
    }
}
