package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.PaymentConfirmationDto;
import com.example.WeddingVenderMngSystem.dto.PaymentRequestDto;
import com.example.WeddingVenderMngSystem.dto.PaymentResponseDto;
import com.example.WeddingVenderMngSystem.entity.Booking;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Payment;
import com.example.WeddingVenderMngSystem.entity.Payment.PaymentStatus;
import com.example.WeddingVenderMngSystem.repository.BookingRepository;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Create a payment intent for a booking
     */
    public PaymentResponseDto createPaymentIntent(Long userId, PaymentRequestDto paymentRequest) throws StripeException {
        logger.info("Creating payment intent for user: {}, booking: {}", userId, paymentRequest.getBookingId());
        
        // Get customer by user ID
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> {
                    logger.error("Customer not found for user ID: {}", userId);
                    return new RuntimeException("Customer not found for user ID: " + userId);
                });
        
        logger.info("Found customer: {}", customer.getCustomerId());
        
        // Get booking and verify it belongs to the customer and is accepted
        Booking booking = bookingRepository.findByBookingIdAndCustomer_CustomerId(
                paymentRequest.getBookingId(), customer.getCustomerId())
                .orElseThrow(() -> {
                    logger.error("Booking not found or not authorized. BookingId: {}, CustomerId: {}", 
                            paymentRequest.getBookingId(), customer.getCustomerId());
                    return new RuntimeException("Booking not found or not authorized");
                });
        
        logger.info("Found booking: {}, status: {}", booking.getBookingId(), booking.getStatus());
        
        if (booking.getStatus() != Booking.BookingStatus.ACCEPTED) {
            logger.error("Booking {} is not in ACCEPTED status, current status: {}", 
                    booking.getBookingId(), booking.getStatus());
            throw new RuntimeException("Booking must be accepted by vendor before payment");
        }
        
        // Check if payment already exists for this booking
        if (paymentRepository.findByBooking_BookingId(booking.getBookingId()).isPresent()) {
            logger.error("Payment already exists for booking: {}", booking.getBookingId());
            throw new RuntimeException("Payment already exists for this booking");
        }
        
        // Convert amount to cents (Stripe expects amounts in smallest currency unit)
        long amountInCents = paymentRequest.getAmount().multiply(new BigDecimal("100")).longValue();
        logger.info("Creating Stripe PaymentIntent for amount: {} cents", amountInCents);
        
        // Create Stripe PaymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(paymentRequest.getCurrency().toLowerCase())
                .putMetadata("bookingId", booking.getBookingId().toString())
                .putMetadata("customerId", customer.getCustomerId().toString())
                .putMetadata("serviceId", booking.getService().getServiceId().toString())
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();
        
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        logger.info("Stripe PaymentIntent created: {}", paymentIntent.getId());
        
        // Create payment record in database
        Payment payment = new Payment(
                booking,
                paymentRequest.getAmount(),
                paymentIntent.getId(),
                paymentIntent.getClientSecret()
        );
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setStatus(PaymentStatus.PENDING);
        
        payment = paymentRepository.save(payment);
        logger.info("Payment record saved with ID: {}", payment.getPaymentId());
        
        return convertToResponseDto(payment);
    }

    /**
     * Confirm payment after successful payment on frontend
     */
    public PaymentResponseDto confirmPayment(Long userId, PaymentConfirmationDto confirmationDto) throws StripeException {
        // Get customer by user ID
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        // Find payment by Stripe payment intent ID
        Payment payment = paymentRepository.findByStripePaymentIntentId(confirmationDto.getStripePaymentIntentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        // Verify payment belongs to the customer
        if (!payment.getBooking().getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new RuntimeException("Payment not authorized for this customer");
        }
        
        // Retrieve payment intent from Stripe to check status
        PaymentIntent paymentIntent = PaymentIntent.retrieve(
                confirmationDto.getStripePaymentIntentId(),
                PaymentIntentRetrieveParams.builder().build(),
                null
        );
        
        // Update payment status based on Stripe status
        switch (paymentIntent.getStatus()) {
            case "succeeded":
                payment.setStatus(PaymentStatus.SUCCEEDED);
                payment.setPaidAt(LocalDateTime.now());
                
                // Update booking status to CONFIRMED
                Booking booking = payment.getBooking();
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
                break;
                
            case "processing":
                payment.setStatus(PaymentStatus.PROCESSING);
                break;
                
            case "requires_payment_method":
            case "requires_confirmation":
            case "requires_action":
                payment.setStatus(PaymentStatus.PENDING);
                break;
                
            case "canceled":
                payment.setStatus(PaymentStatus.CANCELLED);
                break;
                
            default:
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Unknown payment status: " + paymentIntent.getStatus());
        }
        
        payment = paymentRepository.save(payment);
        
        return convertToResponseDto(payment);
    }

    /**
     * Get payment by booking ID for customer
     */
    public PaymentResponseDto getPaymentByBookingId(Long userId, Long bookingId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        Payment payment = paymentRepository.findByBookingIdAndCustomerId(bookingId, customer.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        
        return convertToResponseDto(payment);
    }

    /**
     * Get all payments for a customer
     */
    public List<PaymentResponseDto> getCustomerPayments(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Customer not found for user ID: " + userId));
        
        List<Payment> payments = paymentRepository.findByCustomerId(customer.getCustomerId());
        
        return payments.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all payments for a vendor
     */
    public List<PaymentResponseDto> getVendorPayments(Long userId) {
        // This method would require vendor repository lookup
        // Implementation similar to customer but for vendor
        throw new RuntimeException("Method not implemented yet");
    }

    /**
     * Convert Payment entity to PaymentResponseDto
     */
    private PaymentResponseDto convertToResponseDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        
        dto.setPaymentId(payment.getPaymentId());
        dto.setBookingId(payment.getBooking().getBookingId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus());
        dto.setStripePaymentIntentId(payment.getStripePaymentIntentId());
        dto.setStripeClientSecret(payment.getStripeClientSecret());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setPaidAt(payment.getPaidAt());
        dto.setFailureReason(payment.getFailureReason());
        
        // Add booking details
        Booking booking = payment.getBooking();
        if (booking != null) {
            dto.setEventLocation(booking.getEventLocation());
            
            if (booking.getCustomer() != null) {
                dto.setCustomerName(booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName());
            }
            
            if (booking.getService() != null) {
                dto.setServiceName(booking.getService().getName());
                
                if (booking.getService().getVendor() != null) {
                    dto.setVendorBusinessName(booking.getService().getVendor().getBusinessName());
                }
            }
        }
        
        return dto;
    }
}
