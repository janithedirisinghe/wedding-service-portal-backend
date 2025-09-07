package com.example.WeddingVenderMngSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
