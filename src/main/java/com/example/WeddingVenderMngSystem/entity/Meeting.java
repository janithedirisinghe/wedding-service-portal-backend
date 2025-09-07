package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "meetings")
@Getter
@Setter
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingMood meetingMood;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus status;

    @Column(length = 1000)
    private String notes;

    @Column(length = 500)
    private String rejectionReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    // Constructors
    public Meeting() {
        this.requestedAt = LocalDateTime.now();
        this.status = MeetingStatus.PENDING;
    }

    public Meeting(LocalDateTime meetingDateTime, MeetingMood meetingMood, String location, 
                   Customer customer, Vendor vendor, String notes) {
        this();
        this.meetingDateTime = meetingDateTime;
        this.meetingMood = meetingMood;
        this.location = location;
        this.customer = customer;
        this.vendor = vendor;
        this.notes = notes;
    }

    // Enums
    public enum MeetingMood {
        IN_PERSON,
        VIRTUAL,
        PHONE_CALL
    }

    public enum MeetingStatus {
        PENDING,
        CONFIRMED,
        REJECTED,
        COMPLETED,
        CANCELLED
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public LocalDateTime getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(LocalDateTime meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }

    public MeetingMood getMeetingMood() {
        return meetingMood;
    }

    public void setMeetingMood(MeetingMood meetingMood) {
        this.meetingMood = meetingMood;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
