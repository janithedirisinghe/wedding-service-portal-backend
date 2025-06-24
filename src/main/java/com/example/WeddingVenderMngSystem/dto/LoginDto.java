package com.example.WeddingVenderMngSystem.dto;

import com.example.WeddingVenderMngSystem.entity.Role;
import lombok.Getter;
import lombok.Setter;


public class LoginDto {
    private String username;
    private String password;

    private Role Role;


    // Getters and setters...
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
