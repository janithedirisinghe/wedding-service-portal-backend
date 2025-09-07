package com.example.WeddingVenderMngSystem.config;

import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
            
            String token = extractTokenFromCookies(httpRequest);
            
            if (token != null && !token.isEmpty()) {
                try {
                    String username = jwtUtil.extractUsername(token);
                    if (jwtUtil.validateToken(token, username)) {
                        User user = userService.findByUsername(username);
                        if (user != null) {
                            // Store user information in WebSocket session attributes
                            attributes.put("userId", user.getUserId());
                            attributes.put("username", user.getUsername());
                            attributes.put("role", user.getRole().name());
                            return true;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("WebSocket authentication error: " + e.getMessage());
                }
            }
        }
        
        // Authentication failed
        System.err.println("WebSocket authentication failed - no valid token found");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Nothing to do after handshake
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
