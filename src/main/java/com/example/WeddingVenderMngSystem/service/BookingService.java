package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.BookingDecisionDto;
import com.example.WeddingVenderMngSystem.dto.BookingRequestDto;
import com.example.WeddingVenderMngSystem.dto.BookingResponseDto;
import com.example.WeddingVenderMngSystem.entity.Booking;
import com.example.WeddingVenderMngSystem.entity.Booking.BookingStatus;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.NotificationType;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.BookingRepository;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.ServiceRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private NotificationService notificationService;

    /**
     * Create a new booking request
     */
    public BookingResponseDto createBookingRequest(Long userId, BookingRequestDto requestDto) {
        // Get customer by user ID
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        // Get service
        com.example.WeddingVenderMngSystem.entity.Service service = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + requestDto.getServiceId()));
        
        // Create booking
        Booking booking = new Booking(
                requestDto.getEventDate(),
                requestDto.getEventLocation(),
                requestDto.getSpecialRequirements(),
                requestDto.getProposedPrice(),
                customer,
                service
        );
        
        booking = bookingRepository.save(booking);
        
        // Create notification for vendor
        Long vendorUserId = service.getVendor().getUser().getUserId();
        String title = "New Booking Request";
        String message = String.format("You have received a new booking request from %s %s for %s on %s", 
            customer.getFirstName(), 
            customer.getLastName(), 
            service.getName(),
            requestDto.getEventDate());
        
        notificationService.createBookingNotification(
            vendorUserId, 
            NotificationType.BOOKING_REQUEST, 
            title, 
            message, 
            booking.getBookingId()
        );
        
        return convertToResponseDto(booking);
    }

    /**
     * Get all bookings for a customer
     */
    public List<BookingResponseDto> getCustomerBookings(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        List<Booking> bookings = bookingRepository.findByCustomer_CustomerId(customer.getCustomerId());
        
        return bookings.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all bookings for a vendor
     */
    public List<BookingResponseDto> getVendorBookings(Long userId) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found for user ID: " + userId));
        
        List<Booking> bookings = bookingRepository.findByVendorId(vendor.getVenderId());
        
        return bookings.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get pending bookings for a vendor
     */
    public List<BookingResponseDto> getPendingBookingsForVendor(Long userId) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found for user ID: " + userId));
        
        List<Booking> bookings = bookingRepository.findPendingBookingsByVendorId(vendor.getVenderId());
        
        return bookings.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get accepted bookings for a customer (ready for payment)
     */
    public List<BookingResponseDto> getAcceptedBookingsForCustomer(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        List<Booking> bookings = bookingRepository.findByCustomer_CustomerIdAndStatus(
                customer.getCustomerId(), BookingStatus.ACCEPTED);
        
        return bookings.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Vendor accepts or rejects a booking
     */
    public BookingResponseDto respondToBooking(Long userId, Long bookingId, BookingDecisionDto decisionDto) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found for user ID: " + userId));
        
        Booking booking = bookingRepository.findByBookingIdAndVendorId(bookingId, vendor.getVenderId())
                .orElseThrow(() -> new RuntimeException("Booking not found or not authorized"));
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending status");
        }
        
        booking.setStatus(decisionDto.isAccepted() ? BookingStatus.ACCEPTED : BookingStatus.REJECTED);
        booking.setVendorNotes(decisionDto.getVendorNotes());
        booking.setResponseDate(LocalDateTime.now());
        
        booking = bookingRepository.save(booking);
        
        // Create notification for customer
        Long customerUserId = booking.getCustomer().getUser().getUserId();
        String title;
        String message;
        NotificationType notificationType;
        
        if (decisionDto.isAccepted()) {
            title = "Booking Request Accepted";
            message = String.format("Great news! %s has accepted your booking request for %s on %s. You can now proceed with payment.",
                vendor.getBusinessName(),
                booking.getService().getName(),
                booking.getEventDate());
            notificationType = NotificationType.BOOKING_ACCEPTED;
        } else {
            title = "Booking Request Rejected";
            message = String.format("Unfortunately, %s has declined your booking request for %s on %s. Please consider other available vendors.",
                vendor.getBusinessName(),
                booking.getService().getName(),
                booking.getEventDate());
            notificationType = NotificationType.BOOKING_REJECTED;
        }
        
        notificationService.createBookingNotification(
            customerUserId,
            notificationType,
            title,
            message,
            booking.getBookingId()
        );
        
        return convertToResponseDto(booking);
    }

    /**
     * Get a specific booking by ID (for customer)
     */
    public BookingResponseDto getBookingForCustomer(Long userId, Long bookingId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        Booking booking = bookingRepository.findByBookingIdAndCustomer_CustomerId(bookingId, customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Booking not found or not authorized"));
        
        return convertToResponseDto(booking);
    }

    /**
     * Get a specific booking by ID (for vendor)
     */
    public BookingResponseDto getBookingForVendor(Long userId, Long bookingId) {
        Vendor vendor = vendorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found for user ID: " + userId));
        
        Booking booking = bookingRepository.findByBookingIdAndVendorId(bookingId, vendor.getVenderId())
                .orElseThrow(() -> new RuntimeException("Booking not found or not authorized"));
        
        return convertToResponseDto(booking);
    }

    /**
     * Convert Booking entity to BookingResponseDto
     */
    private BookingResponseDto convertToResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        
        dto.setBookingId(booking.getBookingId());
        dto.setEventDate(booking.getEventDate());
        dto.setEventLocation(booking.getEventLocation());
        dto.setSpecialRequirements(booking.getSpecialRequirements());
        dto.setProposedPrice(booking.getProposedPrice());
        dto.setStatus(booking.getStatus());
        dto.setRequestDate(booking.getRequestDate());
        dto.setResponseDate(booking.getResponseDate());
        dto.setVendorNotes(booking.getVendorNotes());
        
        // Service details
        if (booking.getService() != null) {
            dto.setServiceId(booking.getService().getServiceId());
            dto.setServiceName(booking.getService().getName());
            dto.setServiceDescription(booking.getService().getDescription());
            dto.setServicePricing(booking.getService().getPricing());
            
            // Vendor details
            if (booking.getService().getVendor() != null) {
                dto.setVendorId(booking.getService().getVendor().getVenderId());
                dto.setVendorBusinessName(booking.getService().getVendor().getBusinessName());
                dto.setVendorType(booking.getService().getVendor().getVenType());
            }
        }
        
        // Customer details
        if (booking.getCustomer() != null) {
            dto.setCustomerId(booking.getCustomer().getCustomerId());
            dto.setCustomerFirstName(booking.getCustomer().getFirstName());
            dto.setCustomerLastName(booking.getCustomer().getLastName());
        }
        
        return dto;
    }
}
