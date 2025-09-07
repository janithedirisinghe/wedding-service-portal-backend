package com.example.WeddingVenderMngSystem.config;

import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        // Try to extract JWT token from headers
        String token = headerAccessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            
            try {
                String username = jwtUtil.extractUsername(token);
                User user = userService.findByUsername(username);
                
                if (user != null && jwtUtil.validateToken(token, username)) {
                    // Store user information in WebSocket session
                    SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
                    accessor.setSessionAttributes(headerAccessor.getSessionAttributes());
                    accessor.getSessionAttributes().put("userId", user.getUserId());
                    accessor.getSessionAttributes().put("username", user.getUsername());
                    accessor.getSessionAttributes().put("userRole", user.getRole().name());
                }
            } catch (Exception e) {
                System.out.println("Error processing WebSocket authentication: " + e.getMessage());
            }
        }
        
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            System.out.println("User Disconnected: " + username);
        }
    }
}
