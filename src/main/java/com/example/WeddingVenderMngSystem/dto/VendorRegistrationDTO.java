package com.example.WeddingVenderMngSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VendorRegistrationDTO {
    @NotBlank
    private String businessName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
