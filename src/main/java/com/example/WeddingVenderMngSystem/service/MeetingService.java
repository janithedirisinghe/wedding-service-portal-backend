package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.MeetingDTO;
import com.example.WeddingVenderMngSystem.dto.MeetingRequestDTO;
import com.example.WeddingVenderMngSystem.dto.MeetingResponseDTO;
import com.example.WeddingVenderMngSystem.entity.Meeting;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.NotificationType;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.MeetingRepository;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private NotificationService notificationService;

    /**
     * Create a new meeting request from customer
     */
    public MeetingDTO createMeetingRequest(Long userId, MeetingRequestDTO meetingRequestDTO) {
        // Get customer by user ID
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for user ID: " + userId));

        // Get vendor
        Vendor vendor = vendorRepository.findById(meetingRequestDTO.getVendorId())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with ID: " + meetingRequestDTO.getVendorId()));

        // Create meeting entity
        Meeting meeting = new Meeting();
        meeting.setMeetingDateTime(meetingRequestDTO.getMeetingDateTime());
        meeting.setMeetingMood(meetingRequestDTO.getMeetingMood());
        meeting.setLocation(meetingRequestDTO.getLocation());
        meeting.setNotes(meetingRequestDTO.getNotes());
        meeting.setCustomer(customer);
        meeting.setVendor(vendor);
        meeting.setStatus(Meeting.MeetingStatus.PENDING);
        meeting.setRequestedAt(LocalDateTime.now());

        Meeting savedMeeting = meetingRepository.save(meeting);
        
        // Create notification for vendor
        Long vendorUserId = vendor.getUser().getUserId();
        String title = "New Meeting Request";
        String message = String.format("You have received a new meeting request from %s %s scheduled for %s at %s",
            customer.getFirstName(),
            customer.getLastName(),
            meeting.getMeetingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            meeting.getLocation());
        
        notificationService.createMeetingNotification(
            vendorUserId,
            NotificationType.MEETING_REQUEST,
            title,
            message,
            savedMeeting.getMeetingId()
        );
        
        return new MeetingDTO(savedMeeting);
    }

    /**
     * Vendor responds to meeting request
     */
    public MeetingDTO respondToMeetingRequest(Long userId, MeetingResponseDTO responseDTO) {
        // Get vendor by user ID
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        // Get meeting and verify it belongs to this vendor
        Meeting meeting = meetingRepository.findById(responseDTO.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found with ID: " + responseDTO.getMeetingId()));

        if (!meeting.getVendor().getVenderId().equals(vendor.getVenderId())) {
            throw new IllegalArgumentException("Meeting does not belong to this vendor");
        }

        if (meeting.getStatus() != Meeting.MeetingStatus.PENDING) {
            throw new IllegalStateException("Meeting has already been responded to");
        }

        // Update meeting status
        meeting.setStatus(responseDTO.getStatus());
        
        if (responseDTO.getStatus() == Meeting.MeetingStatus.CONFIRMED) {
            meeting.setConfirmedAt(LocalDateTime.now());
        } else if (responseDTO.getStatus() == Meeting.MeetingStatus.REJECTED) {
            meeting.setRejectionReason(responseDTO.getRejectionReason());
        }

        Meeting savedMeeting = meetingRepository.save(meeting);
        
        // Create notification for customer
        Long customerUserId = meeting.getCustomer().getUser().getUserId();
        String title;
        String message;
        NotificationType notificationType;
        
        if (responseDTO.getStatus() == Meeting.MeetingStatus.CONFIRMED) {
            title = "Meeting Request Accepted";
            message = String.format("%s has confirmed your meeting request for %s at %s. Don't forget to attend!",
                vendor.getBusinessName(),
                meeting.getMeetingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                meeting.getLocation());
            notificationType = NotificationType.MEETING_ACCEPTED;
        } else {
            title = "Meeting Request Rejected";
            message = String.format("Unfortunately, %s has declined your meeting request for %s. %s",
                vendor.getBusinessName(),
                meeting.getMeetingDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                responseDTO.getRejectionReason() != null ? "Reason: " + responseDTO.getRejectionReason() : "");
            notificationType = NotificationType.MEETING_REJECTED;
        }
        
        notificationService.createMeetingNotification(
            customerUserId,
            notificationType,
            title,
            message,
            savedMeeting.getMeetingId()
        );
        
        return new MeetingDTO(savedMeeting);
    }

    /**
     * Get all meetings for a customer
     */
    public List<MeetingDTO> getCustomerMeetings(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for user ID: " + userId));

        List<Meeting> meetings = meetingRepository.findByCustomer(customer);
        return meetings.stream()
                .map(MeetingDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get all meetings for a vendor
     */
    public List<MeetingDTO> getVendorMeetings(Long userId) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        List<Meeting> meetings = meetingRepository.findByVendor(vendor);
        return meetings.stream()
                .map(MeetingDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get meetings by status for customer
     */
    public List<MeetingDTO> getCustomerMeetingsByStatus(Long userId, Meeting.MeetingStatus status) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for user ID: " + userId));

        List<Meeting> meetings = meetingRepository.findByCustomerAndStatus(customer, status);
        return meetings.stream()
                .map(MeetingDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get meetings by status for vendor
     */
    public List<MeetingDTO> getVendorMeetingsByStatus(Long userId, Meeting.MeetingStatus status) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        List<Meeting> meetings = meetingRepository.findByVendorAndStatus(vendor, status);
        return meetings.stream()
                .map(MeetingDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming meetings for customer
     */
    public List<MeetingDTO> getUpcomingCustomerMeetings(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found for user ID: " + userId));

        List<Meeting> meetings = meetingRepository.findUpcomingMeetingsByCustomerId(
                customer.getCustomerId(), LocalDateTime.now());
        return meetings.stream()
                .map(MeetingDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming meetings for vendor
     */
    public List<MeetingDTO> getUpcomingVendorMeetings(Long userId) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        List<Meeting> meetings = meetingRepository.findUpcomingMeetingsByVendorId(
                vendor.getVenderId(), LocalDateTime.now());
        return meetings.stream()
                .map(MeetingDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get meeting by ID (with proper authorization)
     */
    public MeetingDTO getMeetingById(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found with ID: " + meetingId));

        // Check if user is either the customer or vendor of this meeting
        boolean isAuthorized = false;
        
        Optional<Customer> customer = customerRepository.findByUser_UserId(userId);
        if (customer.isPresent() && meeting.getCustomer().getCustomerId().equals(customer.get().getCustomerId())) {
            isAuthorized = true;
        }
        
        Optional<Vendor> vendor = vendorRepository.findByUser_UserId(userId);
        if (vendor.isPresent() && meeting.getVendor().getVenderId().equals(vendor.get().getVenderId())) {
            isAuthorized = true;
        }

        if (!isAuthorized) {
            throw new IllegalArgumentException("User not authorized to view this meeting");
        }

        return new MeetingDTO(meeting);
    }

    /**
     * Cancel meeting (can be done by either customer or vendor)
     */
    public MeetingDTO cancelMeeting(Long userId, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found with ID: " + meetingId));

        // Check if user is either the customer or vendor of this meeting
        boolean isAuthorized = false;
        
        Optional<Customer> customer = customerRepository.findByUser_UserId(userId);
        if (customer.isPresent() && meeting.getCustomer().getCustomerId().equals(customer.get().getCustomerId())) {
            isAuthorized = true;
        }
        
        Optional<Vendor> vendor = vendorRepository.findByUser_UserId(userId);
        if (vendor.isPresent() && meeting.getVendor().getVenderId().equals(vendor.get().getVenderId())) {
            isAuthorized = true;
        }

        if (!isAuthorized) {
            throw new IllegalArgumentException("User not authorized to cancel this meeting");
        }

        if (meeting.getStatus() == Meeting.MeetingStatus.COMPLETED || 
            meeting.getStatus() == Meeting.MeetingStatus.CANCELLED) {
            throw new IllegalStateException("Meeting cannot be cancelled in its current state");
        }

        meeting.setStatus(Meeting.MeetingStatus.CANCELLED);
        Meeting savedMeeting = meetingRepository.save(meeting);
        return new MeetingDTO(savedMeeting);
    }

    /**
     * Mark meeting as completed (typically done by vendor)
     */
    public MeetingDTO completeMeeting(Long userId, Long meetingId) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found for user ID: " + userId));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found with ID: " + meetingId));

        if (!meeting.getVendor().getVenderId().equals(vendor.getVenderId())) {
            throw new IllegalArgumentException("Meeting does not belong to this vendor");
        }

        if (meeting.getStatus() != Meeting.MeetingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed meetings can be marked as completed");
        }

        meeting.setStatus(Meeting.MeetingStatus.COMPLETED);
        Meeting savedMeeting = meetingRepository.save(meeting);
        return new MeetingDTO(savedMeeting);
    }
}
