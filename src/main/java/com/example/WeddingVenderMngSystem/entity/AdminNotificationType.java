package com.example.WeddingVenderMngSystem.entity;

public enum AdminNotificationType {
    SUPPORT_REQUEST("Support Request"),
    SERVICE_CREATED("Service Created"),
    REVIEW_RECEIVED("Review Received"),
    PAYMENT_RECEIVED("Payment Received"),
    USER_REGISTERED("User Registered"),
    SYSTEM_ALERT("System Alert");

    private final String displayName;

    AdminNotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
