package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.entity.Role;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(User user) {
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return "User already exists! Try another username.";
            }

            if (user.getRole() == null) {
                user.setRole(Role.CUSTOMER); // Default role
            }

            // Encrypt the password before saving the user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            return "User registered successfully!";
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Error Occer Already used username";
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}



