package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.common.EmailService;
import com.example.WeddingVenderMngSystem.common.OtpService;
import com.example.WeddingVenderMngSystem.dto.LoginDto;
import com.example.WeddingVenderMngSystem.dto.OtpVerificationRequest;
import com.example.WeddingVenderMngSystem.entity.Role;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

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
    public ResponseEntity<Map<String, String>>  registerUser(@RequestBody User user) {
        // Validate role
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER); // Assign default role
        } else if (!EnumSet.of(Role.ADMIN, Role.CUSTOMER, Role.VENDOR).contains(user.getRole())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid role!"));
        }

        // Check if email exists
        if (userService.userExists(user.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email already registered!"));
        }

        // Generate OTP and store it in the user object
        String otp = otpService.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(5)); // OTP expires in 5 minutes
        user.setEnabled(false); // Initially disabled

        // Attempt registration
        String result = userService.registerUser(user);
        if (result.equals("User already exists! Try another username.")) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", result));
        }

        // Send OTP via email
        emailService.sendOtpEmail(user.getEmail(), otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");

        return ResponseEntity.ok(response); // Return map as a JSON response
    }

    // ------------------- OTP Verification -------------------
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody OtpVerificationRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found!"));
        }

        if (otpService.isOtpValid(user, request.getOtp())) {
            user.setEnabled(true); // Activate the account
            user.setOtpCode(null); // Clear OTP after successful verification
            userService.registerUser(user);
            return ResponseEntity.ok(Collections.singletonMap("message", "Account verified successfully!"));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid or expired OTP!"));
        }
    }

}
