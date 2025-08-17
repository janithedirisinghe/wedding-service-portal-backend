package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.Meeting;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingRequestDTO {
    @NotNull(message = "Meeting date and time is required")
    @Future(message = "Meeting date and time must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingDateTime;
    
    @NotNull(message = "Meeting mood is required")
    private Meeting.MeetingMood meetingMood;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Vendor ID is required")
    private Long vendorId;
    
    private String notes;

    public MeetingRequestDTO() {}

    public MeetingRequestDTO(LocalDateTime meetingDateTime, Meeting.MeetingMood meetingMood, 
                           String location, Long vendorId, String notes) {
        this.meetingDateTime = meetingDateTime;
        this.meetingMood = meetingMood;
        this.location = location;
        this.vendorId = vendorId;
        this.notes = notes;
    }

    public LocalDateTime getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(LocalDateTime meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }

    public Meeting.MeetingMood getMeetingMood() {
        return meetingMood;
    }

    public void setMeetingMood(Meeting.MeetingMood meetingMood) {
        this.meetingMood = meetingMood;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
