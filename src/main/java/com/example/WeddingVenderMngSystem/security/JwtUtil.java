package com.example.WeddingVenderMngSystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        // Create a consistent key from the secret string
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        // Ensure the key is at least 256 bits (32 bytes) for HS256
        if (keyBytes.length < 32) {
            // Pad the key if it's too short
            byte[] paddedKey = new byte[32];
            System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
            keyBytes = paddedKey;
        }
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // Generate JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(getSigningKey()) // Use consistent key
                .compact();
    }

    // Validate JWT Token
    public boolean validateToken(String token, String username) {
        String user = extractUsername(token);
        return (username.equals(user) && !isTokenExpired(token));
    }

    // Extract username from JWT Token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract expiration date from JWT Token
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    // Extract claims from JWT Token
    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.err.println("JWT parsing error: " + e.getMessage());
            throw e;
        }
    }

    // Check if JWT Token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
