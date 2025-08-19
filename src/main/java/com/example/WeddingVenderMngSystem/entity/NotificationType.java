package com.example.WeddingVenderMngSystem.entity;

public enum NotificationType {
    BOOKING_REQUEST("Booking Request"),
    BOOKING_ACCEPTED("Booking Accepted"),
    BOOKING_REJECTED("Booking Rejected"),
    BOOKING_CANCELLED("Booking Cancelled"),
    BOOKING_COMPLETED("Booking Completed"),
    
    MEETING_REQUEST("Meeting Request"),
    MEETING_ACCEPTED("Meeting Accepted"),
    MEETING_REJECTED("Meeting Rejected"),
    MEETING_CANCELLED("Meeting Cancelled"),
    MEETING_REMINDER("Meeting Reminder"),
    
    PAYMENT_RECEIVED("Payment Received"),
    PAYMENT_PENDING("Payment Pending"),
    PAYMENT_FAILED("Payment Failed"),
    
    REVIEW_RECEIVED("Review Received"),
    FOLLOW_REQUEST("Follow Request"),
    
    SYSTEM_ANNOUNCEMENT("System Announcement"),
    GENERAL("General Notification");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
