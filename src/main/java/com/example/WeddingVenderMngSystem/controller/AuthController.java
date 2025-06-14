package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.common.EmailService;
import com.example.WeddingVenderMngSystem.common.OtpService;
import com.example.WeddingVenderMngSystem.dto.LoginDto;
import com.example.WeddingVenderMngSystem.dto.OtpVerificationRequest;
import com.example.WeddingVenderMngSystem.entity.Role;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.UserService;
import com.example.WeddingVenderMngSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private VendorService vendorService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // Retrieve user details
        User user = userService.findByUsername(loginDto.getUsername());
        if (user == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not found!"));
        }

        // Generate JWT Token
        String token = jwtUtil.generateToken(loginDto.getUsername());

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("token", "Bearer " + token);
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole().toString());

        return ResponseEntity.ok(response);
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
        Optional<User> result = userService.registerUser(user);
        if (result.equals("User already exists! Try another username.")) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User already exists! Try another username."));
        }

        // Send OTP via email
        emailService.sendOtpEmail(user.getEmail(), otp);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        response.put("userId", result.get().getUserId().toString());// Send userId
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

    @PostMapping("/vendor-complete-info")
    public ResponseEntity<Map<String, String>> registerVendor(@RequestParam Long userId, @RequestBody Vendor vendorDetails) {
        try {
            Vendor savedVendor = vendorService.registerVendor(userId, vendorDetails);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Vendor registered successfully!");
            response.put("vendorId", savedVendor.getVenderId().toString());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

}
