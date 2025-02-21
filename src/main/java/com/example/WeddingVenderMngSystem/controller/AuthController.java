package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.LoginDto;
import com.example.WeddingVenderMngSystem.entity.Role;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // Generate JWT Token
        String token = jwtUtil.generateToken(loginDto.getUsername());
        return "Bearer " + token;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER); // Assign default role
        } else if (!EnumSet.of(Role.ADMIN, Role.CUSTOMER, Role.VENDOR).contains(user.getRole())) {
            return "Invalid role!"; // Reject if role is invalid
        }

        userService.registerUser(user);
        return "User registered successfully!";
    }
}
