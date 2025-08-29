package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.common.EmailService;
import com.example.WeddingVenderMngSystem.common.OtpService;
import com.example.WeddingVenderMngSystem.dto.ChangePasswordRequest;
import com.example.WeddingVenderMngSystem.dto.CustomerDTO;
import com.example.WeddingVenderMngSystem.dto.LoginDto;
import com.example.WeddingVenderMngSystem.dto.OtpVerificationRequest;
import com.example.WeddingVenderMngSystem.entity.Role;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.UserService;
import com.example.WeddingVenderMngSystem.service.VendorService;
import com.example.WeddingVenderMngSystem.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
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

        ResponseCookie cookie = ResponseCookie.from("auth_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60*60)
                .sameSite("Strict")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        // Prepare response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful");
        responseBody.put("userId", user.getUserId());
        responseBody.put("username", user.getUsername());
        responseBody.put("role", user.getRole().toString());

        return ResponseEntity.ok(responseBody);
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

    @PostMapping("/customer-complete-info")
    public ResponseEntity<Map<String, String>> registerCustomer(@RequestParam Long userId, @RequestBody Customer customerDetails) {
        try {
            Customer savedCustomer = customerService.registerCustomer(userId, customerDetails);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Customer registered successfully!");
            response.put("customerId", savedCustomer.getCustomerId().toString());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/customer-profile")
    public ResponseEntity<Map<String, Object>> getCustomerProfile(@RequestParam Long userId) {
        try {
            CustomerDTO customer = customerService.getCustomerDTOByUserId(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("customer", customer);
            response.put("message", "Customer profile retrieved successfully!");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // ------------------- Change Password API -------------------
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User not authenticated"));
            }

            String username = authentication.getName();
            
            // Validate password confirmation
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New password and confirmation do not match"));
            }

            // Validate password length
            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New password must be at least 6 characters long"));
            }

            // Check if new password is different from current password
            if (request.getCurrentPassword().equals(request.getNewPassword())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New password must be different from current password"));
            }

            // Attempt to change password
            boolean success = userService.changePassword(username, request.getCurrentPassword(), request.getNewPassword());
            
            if (success) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully"));
            } else {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Current password is incorrect"));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "An error occurred while changing password: " + e.getMessage()));
        }
    }
}
