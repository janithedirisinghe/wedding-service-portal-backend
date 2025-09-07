package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.*;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.security.JwtUtil;
import com.example.WeddingVenderMngSystem.service.ChatService;
import com.example.WeddingVenderMngSystem.service.UserService;
import com.example.WeddingVenderMngSystem.service.VendorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    /**
     * Get all vendors for chat functionality
     */
    @GetMapping("/vendors")
    public ResponseEntity<List<VendorListDTO>> getAllVendorsForChat() {
        try {
            List<VendorListDTO> vendors = vendorService.getAllVendorsAsDTO();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Start a new chat with a vendor (Customer only)
     */
    @PostMapping("/start")
    public ResponseEntity<ChatRoomDTO> startChat(
            HttpServletRequest request,
            @RequestBody StartChatRequest startChatRequest) {
        try {
            Long userId = getUserIdFromRequest(request);
            ChatRoomDTO chatRoom = chatService.startChat(userId, startChatRequest);
            return ResponseEntity.ok(chatRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Send a message in a chat room
     */
    @PostMapping("/message")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            HttpServletRequest request,
            @RequestBody SendMessageRequest messageRequest) {
        try {
            Long userId = getUserIdFromRequest(request);
            ChatMessageDTO message = chatService.sendMessage(userId, messageRequest);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get customer's chat rooms by userId
     */
    @GetMapping("/customer/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getCustomerChatRooms(@RequestParam Long userId) {
        try {
            List<ChatRoomDTO> chatRooms = chatService.getCustomerChatRooms(userId);
            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get vendor's chat rooms by userId
     */
    @GetMapping("/vendor/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getVendorChatRooms(@RequestParam Long userId) {
        try {
            List<ChatRoomDTO> chatRooms = chatService.getVendorChatRooms(userId);
            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get messages in a specific chat room
     */
    @GetMapping("/room/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(
            HttpServletRequest request,
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            Long userId = getUserIdFromRequest(request);
            List<ChatMessageDTO> messages = chatService.getChatMessages(userId, chatRoomId, page, size);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mark messages as read in a chat room
     */
    @PutMapping("/room/{chatRoomId}/mark-read")
    public ResponseEntity<Void> markMessagesAsRead(
            HttpServletRequest request,
            @PathVariable Long chatRoomId) {
        try {
            Long userId = getUserIdFromRequest(request);
            chatService.markMessagesAsRead(chatRoomId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Extract user ID from JWT token in request
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new RuntimeException("No token provided");
        }

        String username = jwtUtil.extractUsername(token);
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user.getUserId();
    }

    /**
     * Extract JWT token from HTTP-only cookies or Authorization header as fallback
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // First try to get token from HTTP-only cookie
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Fallback: check Authorization header (for tools like Postman)
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
