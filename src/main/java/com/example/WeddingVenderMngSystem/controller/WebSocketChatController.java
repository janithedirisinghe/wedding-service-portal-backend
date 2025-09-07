package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.ChatMessageDTO;
import com.example.WeddingVenderMngSystem.dto.SendMessageRequest;
import com.example.WeddingVenderMngSystem.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketChatController {

    @Autowired
    private ChatService chatService;

    /**
     * Handle incoming WebSocket chat messages
     */
    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatMessageDTO sendMessage(
            @DestinationVariable Long chatRoomId,
            SendMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor,
            Principal principal) {
        
        try {
            // Get user ID from session attributes (set during WebSocket connection)
            Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
            if (userId == null) {
                throw new RuntimeException("User not authenticated");
            }
            
            // Ensure the request has the correct chat room ID
            request.setChatRoomId(chatRoomId);
            
            // Send the message through the chat service
            return chatService.sendMessage(userId, request);
            
        } catch (Exception e) {
            // Return error message
            ChatMessageDTO errorMessage = new ChatMessageDTO();
            errorMessage.setContent("Error sending message: " + e.getMessage());
            return errorMessage;
        }
    }

    /**
     * Handle user joining a chat room
     */
    @MessageMapping("/chat/{chatRoomId}/join")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatMessageDTO userJoin(
            @DestinationVariable Long chatRoomId,
            SimpMessageHeaderAccessor headerAccessor) {
        
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        // Create a system message for user joining
        ChatMessageDTO joinMessage = new ChatMessageDTO();
        joinMessage.setChatRoomId(chatRoomId);
        joinMessage.setContent(username + " joined the chat");
        joinMessage.setSenderName("System");
        joinMessage.setSenderType("SYSTEM");
        
        return joinMessage;
    }

    /**
     * Handle user leaving a chat room
     */
    @MessageMapping("/chat/{chatRoomId}/leave")
    @SendTo("/topic/chat/{chatRoomId}")
    public ChatMessageDTO userLeave(
            @DestinationVariable Long chatRoomId,
            SimpMessageHeaderAccessor headerAccessor) {
        
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        
        // Create a system message for user leaving
        ChatMessageDTO leaveMessage = new ChatMessageDTO();
        leaveMessage.setChatRoomId(chatRoomId);
        leaveMessage.setContent(username + " left the chat");
        leaveMessage.setSenderName("System");
        leaveMessage.setSenderType("SYSTEM");
        
        return leaveMessage;
    }
}
