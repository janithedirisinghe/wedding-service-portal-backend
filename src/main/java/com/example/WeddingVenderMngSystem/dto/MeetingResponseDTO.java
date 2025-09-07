package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.Meeting;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class MeetingResponseDTO {
    @NotNull(message = "Meeting ID is required")
    private Long meetingId;
    
    @NotNull(message = "Response status is required")
    private Meeting.MeetingStatus status;
    
    private String rejectionReason;

    public MeetingResponseDTO() {}

    public MeetingResponseDTO(Long meetingId, Meeting.MeetingStatus status) {
        this.meetingId = meetingId;
        this.status = status;
    }

    public MeetingResponseDTO(Long meetingId, Meeting.MeetingStatus status, String rejectionReason) {
        this.meetingId = meetingId;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Meeting.MeetingStatus getStatus() {
        return status;
    }

    public void setStatus(Meeting.MeetingStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
