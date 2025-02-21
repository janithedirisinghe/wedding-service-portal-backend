package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
    private String businessName;
    private String availability;
    private String location;
    private Date weddingDate;
    private Double budget;
}