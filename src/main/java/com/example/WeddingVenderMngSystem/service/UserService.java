package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import com.example.WeddingVenderMngSystem.entity.Role;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminNotificationService adminNotificationService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> registerUser(User user) {
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return Optional.empty();
            }

            if (user.getRole() == null) {
                user.setRole(Role.CUSTOMER); // Default role
            }

            // Encrypt the password before saving the user
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        } catch (Exception e) {
            System.out.println(e);
        }

        User savedUser = userRepository.save(user);

        // Create admin notification for new user registration
        String title = "New User Registered";
        String message = "A new " + savedUser.getRole().name().toLowerCase() + " has registered: " + savedUser.getUsername() + " (" + savedUser.getEmail() + ")";
        adminNotificationService.createNotification(
            AdminNotificationType.USER_REGISTERED,
            title,
            message,
            savedUser.getUserId(),
            NotificationPriority.NORMAL
        );

        return Optional.of(savedUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        try {
            User user = findByUsername(username);
            if (user == null) {
                return false;
            }

            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return false;
            }

            // Update password with new encrypted password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;

        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
            return false;
        }
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean resetPasswordWithOtp(String email, String otp, String newPassword) {
        try {
            User user = findByEmail(email);
            if (user == null) {
                return false;
            }

            // Check if OTP is valid
            if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
                return false;
            }

            // Check if OTP is expired
            if (user.getOtpExpiration() == null || user.getOtpExpiration().isBefore(java.time.LocalDateTime.now())) {
                return false;
            }

            // Update password and clear OTP
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setOtpCode(null);
            user.setOtpExpiration(null);
            userRepository.save(user);
            return true;

        } catch (Exception e) {
            System.out.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}



