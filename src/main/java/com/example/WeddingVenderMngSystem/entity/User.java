package com.example.WeddingVenderMngSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String password;

    private String otpCode; // OTP field
    private LocalDateTime otpExpiration; // Expiration time for OTP

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Vendor vendor;

    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getPassword(){
        return password;
    }


    public void setPassword(String password) {
        this.password = password; // No encoding here
    }

    public Role getRole(){
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpCode(){
        return this.otpCode;
    }

    public LocalDateTime getOtpExpiration(){
        return this.otpExpiration;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public void setOtpExpiration(LocalDateTime otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }


}