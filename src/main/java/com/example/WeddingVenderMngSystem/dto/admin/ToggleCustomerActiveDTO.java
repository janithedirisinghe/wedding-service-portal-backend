package com.example.WeddingVenderMngSystem.dto.admin;

public class ToggleCustomerActiveDTO {
    private Long customerId;
    private boolean isActive;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
}
