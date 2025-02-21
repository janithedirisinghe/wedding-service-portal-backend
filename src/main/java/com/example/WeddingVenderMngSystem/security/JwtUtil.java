package com.example.WeddingVenderMngSystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generate JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(SECRET_KEY) // Use Secure Key
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
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if JWT Token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
