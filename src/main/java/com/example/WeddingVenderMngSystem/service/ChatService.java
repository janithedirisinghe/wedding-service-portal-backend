package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.*;
import com.example.WeddingVenderMngSystem.entity.*;
import com.example.WeddingVenderMngSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Start a new chat between customer and vendor
     */
    public ChatRoomDTO startChat(Long userId, StartChatRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.getRole().equals(Role.CUSTOMER)) {
            throw new RuntimeException("Only customers can start chats with vendors");
        }
        
        Customer customer = user.getCustomer();
        if (customer == null) {
            throw new RuntimeException("Customer profile not found");
        }
        
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        
        // Check if chat room already exists
        Optional<ChatRoom> existingRoom = chatRoomRepository
                .findByCustomer_CustomerIdAndVendor_VenderId(customer.getCustomerId(), vendor.getVenderId());
        
        ChatRoom chatRoom;
        if (existingRoom.isPresent()) {
            chatRoom = existingRoom.get();
        } else {
            // Create new chat room
            chatRoom = new ChatRoom();
            chatRoom.setCustomer(customer);
            chatRoom.setVendor(vendor);
            chatRoom.setRoomName("customer_" + customer.getCustomerId() + "_vendor_" + vendor.getVenderId());
            chatRoom.setStatus(ChatRoom.ChatStatus.ACTIVE);
            chatRoom = chatRoomRepository.save(chatRoom);
        }
        
        // Send initial message if provided
        if (request.getInitialMessage() != null && !request.getInitialMessage().trim().isEmpty()) {
            SendMessageRequest messageRequest = new SendMessageRequest();
            messageRequest.setChatRoomId(chatRoom.getChatRoomId());
            messageRequest.setContent(request.getInitialMessage());
            sendMessage(userId, messageRequest);
        }
        
        return convertToDTO(chatRoom);
    }

    /**
     * Send a message in a chat room
     */
    public ChatMessageDTO sendMessage(Long userId, SendMessageRequest request) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        // Verify user has access to this chat room
        if (!hasAccessToRoom(sender, chatRoom)) {
            throw new RuntimeException("Access denied to this chat room");
        }
        
        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType());
        message.setAttachmentUrl(request.getAttachmentUrl());
        message.setAttachmentType(request.getAttachmentType());
        message.setSentAt(LocalDateTime.now());
        
        message = chatMessageRepository.save(message);
        
        // Update chat room's last message time
        chatRoom.updateLastMessageTime();
        chatRoomRepository.save(chatRoom);
        
        ChatMessageDTO messageDTO = convertMessageToDTO(message);
        
        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getChatRoomId(), messageDTO);
        
        return messageDTO;
    }

    /**
     * Get user's chat rooms
     */
    public List<ChatRoomDTO> getUserChatRooms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserIdOrderByLastMessageAtDesc(userId);
        
        return chatRooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get customer's chat rooms by userId
     */
    public List<ChatRoomDTO> getCustomerChatRooms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCustomer() == null) {
            throw new RuntimeException("User is not a customer");
        }

        Long customerId = user.getCustomer().getCustomerId();
        List<ChatRoom> chatRooms = chatRoomRepository.findByCustomerIdOrderByLastMessageAtDesc(customerId);

        return chatRooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get vendor's chat rooms by userId
     */
    public List<ChatRoomDTO> getVendorChatRooms(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getVendor() == null) {
            throw new RuntimeException("User is not a vendor");
        }

        Long vendorId = user.getVendor().getVenderId();
        List<ChatRoom> chatRooms = chatRoomRepository.findByVendorIdOrderByLastMessageAtDesc(vendorId);

        return chatRooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get messages in a chat room
     */
    public List<ChatMessageDTO> getChatMessages(Long userId, Long chatRoomId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        if (!hasAccessToRoom(user, chatRoom)) {
            throw new RuntimeException("Access denied to this chat room");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        List<ChatMessage> messages = chatMessageRepository
                .findByChatRoom_ChatRoomIdOrderBySentAtDesc(chatRoomId, pageable)
                .getContent();
        
        // Mark messages as read
        markMessagesAsRead(chatRoomId, userId);
        
        // Reverse the list to show oldest first
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(this::convertMessageToDTO)
                .collect(Collectors.toList());
        
        // Reverse to show chronological order (oldest first)
        java.util.Collections.reverse(messageDTOs);
        
        return messageDTOs;
    }

    /**
     * Mark messages as read
     */
    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository
                .findUnreadMessages(chatRoomId, userId);
        
        unreadMessages.forEach(ChatMessage::markAsRead);
        chatMessageRepository.saveAll(unreadMessages);
    }

    /**
     * Check if user has access to chat room
     */
    private boolean hasAccessToRoom(User user, ChatRoom chatRoom) {
        if (user.getRole().equals(Role.CUSTOMER)) {
            return chatRoom.getCustomer().getUser().getUserId().equals(user.getUserId());
        } else if (user.getRole().equals(Role.VENDOR)) {
            return chatRoom.getVendor().getUser().getUserId().equals(user.getUserId());
        }
        return false;
    }

    /**
     * Convert ChatRoom entity to DTO
     */
    private ChatRoomDTO convertToDTO(ChatRoom chatRoom) {
        ChatRoomDTO dto = new ChatRoomDTO();
        dto.setChatRoomId(chatRoom.getChatRoomId());
        dto.setRoomName(chatRoom.getRoomName());
        dto.setCustomerId(chatRoom.getCustomer().getCustomerId());
        dto.setCustomerName(chatRoom.getCustomer().getUser().getUsername());
        dto.setCustomerProfileImageUrl(chatRoom.getCustomer().getProfileImageUrl());
        dto.setVendorId(chatRoom.getVendor().getVenderId());
        dto.setVendorName(chatRoom.getVendor().getUser().getUsername());
        dto.setVendorBusinessName(chatRoom.getVendor().getBusinessName());
        dto.setVendorProfileImageUrl(chatRoom.getVendor().getProfileImageUrl());
        dto.setCreatedAt(chatRoom.getCreatedAt());
        dto.setLastMessageAt(chatRoom.getLastMessageAt());
        dto.setStatus(chatRoom.getStatus());
        
        // Get recent messages
        Pageable pageable = PageRequest.of(0, 5);
        List<ChatMessage> recentMessages = chatMessageRepository
                .findRecentMessagesByChatRoomId(chatRoom.getChatRoomId(), pageable);
        
        dto.setRecentMessages(recentMessages.stream()
                .map(this::convertMessageToDTO)
                .collect(Collectors.toList()));
        
        return dto;
    }

    /**
     * Convert ChatMessage entity to DTO
     */
    private ChatMessageDTO convertMessageToDTO(ChatMessage message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setChatRoomId(message.getChatRoom().getChatRoomId());
        dto.setSenderId(message.getSender().getUserId());
        dto.setSenderName(message.getSender().getUsername());
        dto.setSenderType(message.getSender().getRole().name());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        dto.setMessageType(message.getMessageType());
        dto.setStatus(message.getStatus());
        dto.setReadAt(message.getReadAt());
        dto.setAttachmentUrl(message.getAttachmentUrl());
        dto.setAttachmentType(message.getAttachmentType());
        return dto;
    }
}
