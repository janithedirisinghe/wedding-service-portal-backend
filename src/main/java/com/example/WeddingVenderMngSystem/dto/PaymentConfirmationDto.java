package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentConfirmationDto {
    private String stripePaymentIntentId;
    
    // Constructors
    public PaymentConfirmationDto() {}
    
    public PaymentConfirmationDto(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }
}
