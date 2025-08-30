package com.example.WeddingVenderMngSystem.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + ". It is valid for 5 minutes.");
        mailSender.send(message);
    }

    public void sendPasswordResetOtpEmail(String to, String otp, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Hello " + username + ",\n\n" +
                "You have requested to reset your password. Your OTP code is: " + otp + "\n" +
                "This OTP is valid for 5 minutes only.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\nWedding Service Provider Team");
        mailSender.send(message);
    }

    public void sendPasswordChangedConfirmationEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Successfully Changed");
        message.setText("Hello " + username + ",\n\n" +
                "Your password has been successfully changed. If you did not make this change, " +
                "please contact our support team immediately.\n\n" +
                "Best regards,\nWedding Service Provider Team");
        mailSender.send(message);
    }
}
