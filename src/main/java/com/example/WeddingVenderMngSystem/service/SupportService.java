package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.SupportDTO;
import com.example.WeddingVenderMngSystem.entity.Support;
import com.example.WeddingVenderMngSystem.entity.User;
import com.example.WeddingVenderMngSystem.repository.SupportRepository;
import com.example.WeddingVenderMngSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupportService {

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminNotificationService adminNotificationService;

    public List<SupportDTO> getAllSupports() {
        return supportRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SupportDTO getSupportById(Long supportId) {
        return supportRepository.findById(supportId).map(this::convertToDTO).orElse(null);
    }

    public List<SupportDTO> getSupportsByUserId(Long userId) {
        return supportRepository.findByUser_UserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<SupportDTO> getSupportsBySeverity(Support.Severity severity) {
        return supportRepository.findBySeverity(severity).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SupportDTO createSupport(SupportDTO supportDTO) {
        Support support = new Support();
        support.setTopic(supportDTO.getTopic());
        support.setDescription(supportDTO.getDescription());
        support.setSeverity(supportDTO.getSeverity());
        support.setCreatedDate(LocalDateTime.now());

        User user = userRepository.findById(supportDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + supportDTO.getUserId()));
        support.setUser(user);

        Support savedSupport = supportRepository.save(support);

        // Create admin notification for new support request
        String title = "New Support Request: " + support.getTopic();
        String message = "User " + user.getUsername() + " (" + user.getEmail() + ") has submitted a support request with severity: " + support.getSeverity();
        adminNotificationService.createNotification(
            com.example.WeddingVenderMngSystem.entity.AdminNotificationType.SUPPORT_REQUEST,
            title,
            message,
            savedSupport.getSupportId(),
            com.example.WeddingVenderMngSystem.entity.NotificationPriority.HIGH
        );

        return convertToDTO(savedSupport);
    }

    public SupportDTO updateSupport(Long supportId, SupportDTO supportDTO) {
        Optional<Support> optionalSupport = supportRepository.findById(supportId);
        if (optionalSupport.isPresent()) {
            Support support = optionalSupport.get();
            support.setTopic(supportDTO.getTopic());
            support.setDescription(supportDTO.getDescription());
            support.setSeverity(supportDTO.getSeverity());
            support.setReplyMessage(supportDTO.getReplyMessage());
            if (supportDTO.getReplyMessage() != null && !supportDTO.getReplyMessage().isEmpty()) {
                support.setReplyDate(LocalDateTime.now());
            }
            Support updatedSupport = supportRepository.save(support);
            return convertToDTO(updatedSupport);
        }
        return null;
    }

    public void deleteSupport(Long supportId) {
        supportRepository.deleteById(supportId);
    }

    private SupportDTO convertToDTO(Support support) {
        SupportDTO dto = new SupportDTO();
        dto.setSupportId(support.getSupportId());
        dto.setTopic(support.getTopic());
        dto.setDescription(support.getDescription());
        dto.setReplyMessage(support.getReplyMessage());
        dto.setSeverity(support.getSeverity());
        dto.setCreatedDate(support.getCreatedDate());
        dto.setReplyDate(support.getReplyDate());

        if (support.getUser() != null) {
            dto.setUserId(support.getUser().getUserId());
            dto.setUserName(support.getUser().getUsername());
            dto.setUserRole(support.getUser().getRole().name());
        }

        return dto;
    }
}
