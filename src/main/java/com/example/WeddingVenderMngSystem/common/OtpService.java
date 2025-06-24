package com.example.WeddingVenderMngSystem.common;

import com.example.WeddingVenderMngSystem.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final Random random = new Random();

    // Generate 6-digit OTP
    public String generateOtp() {
        return String.format("%06d", random.nextInt(1000000));
    }

    // Validate OTP expiration time
    public boolean isOtpValid(User user, String enteredOtp) {
        return user.getOtpCode() != null &&
                user.getOtpCode().equals(enteredOtp) &&
                user.getOtpExpiration().isAfter(LocalDateTime.now());
    }
}
