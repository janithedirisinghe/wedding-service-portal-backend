package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDto {
    private Long bookingId;
    private BigDecimal amount;
    private String currency = "USD";
    
    // Constructors
    public PaymentRequestDto() {}
    
    public PaymentRequestDto(Long bookingId, BigDecimal amount) {
        this.bookingId = bookingId;
        this.amount = amount;
    }
    
    public PaymentRequestDto(Long bookingId, BigDecimal amount, String currency) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
