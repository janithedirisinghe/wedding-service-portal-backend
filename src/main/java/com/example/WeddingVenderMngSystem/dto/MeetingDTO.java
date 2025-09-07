package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.Meeting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingDTO {
    private Long meetingId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingDateTime;
    
    private Meeting.MeetingMood meetingMood;
    private String location;
    private Meeting.MeetingStatus status;
    private String notes;
    private String rejectionReason;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmedAt;
    
    // Customer and Vendor information
    private Long customerId;
    private String customerName;
    private String customerEmail;
    
    private Long vendorId;
    private String vendorBusinessName;
    private String vendorEmail;

    // Constructors
    public MeetingDTO() {}

    public MeetingDTO(Meeting meeting) {
        this.meetingId = meeting.getMeetingId();
        this.meetingDateTime = meeting.getMeetingDateTime();
        this.meetingMood = meeting.getMeetingMood();
        this.location = meeting.getLocation();
        this.status = meeting.getStatus();
        this.notes = meeting.getNotes();
        this.rejectionReason = meeting.getRejectionReason();
        this.requestedAt = meeting.getRequestedAt();
        this.confirmedAt = meeting.getConfirmedAt();
        
        if (meeting.getCustomer() != null) {
            this.customerId = meeting.getCustomer().getCustomerId();
            this.customerName = meeting.getCustomer().getFirstName() + " " + meeting.getCustomer().getLastName();
            if (meeting.getCustomer().getUser() != null) {
                this.customerEmail = meeting.getCustomer().getUser().getEmail();
            }
        }
        
        if (meeting.getVendor() != null) {
            this.vendorId = meeting.getVendor().getVenderId();
            this.vendorBusinessName = meeting.getVendor().getBusinessName();
            if (meeting.getVendor().getUser() != null) {
                this.vendorEmail = meeting.getVendor().getUser().getEmail();
            }
        }
    }

}
