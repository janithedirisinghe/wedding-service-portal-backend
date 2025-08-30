package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.ServiceDTO;
import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import com.example.WeddingVenderMngSystem.entity.NotificationPriority;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.ServiceRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import com.example.WeddingVenderMngSystem.repository.FollowerRepository;
import com.example.WeddingVenderMngSystem.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AdminNotificationService adminNotificationService;

    // Create a new Service
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        // Retrieve vendor via userId (preferred) instead of requiring vendorId in request
        Vendor vendor = null;
        if (serviceDTO.getUserId() != null) {
            vendor = vendorRepository.findByUser_UserId(serviceDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found for userId: " + serviceDTO.getUserId()));
        } else if (serviceDTO.getVendorId() != null) { // backward compatibility
            vendor = vendorRepository.findById(serviceDTO.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found for vendorId: " + serviceDTO.getVendorId()));
        } else {
            throw new RuntimeException("Either userId or vendorId must be provided");
        }

        com.example.WeddingVenderMngSystem.entity.Service service = new com.example.WeddingVenderMngSystem.entity.Service();
        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setPricing(serviceDTO.getPricing());
        service.setVendor(vendor);
    // Optional new fields (only override defaults if provided)
    if (serviceDTO.getStatus() != null) service.setStatus(serviceDTO.getStatus());
    if (serviceDTO.getPricingModel() != null) service.setPricingModel(serviceDTO.getPricingModel());
    if (serviceDTO.getAdvancePercentage() != null) service.setAdvancePercentage(serviceDTO.getAdvancePercentage());
    if (serviceDTO.getDiscountPercent() != null) service.setDiscountPercent(serviceDTO.getDiscountPercent());
    if (serviceDTO.getBookBeforeDays() != null) service.setBookBeforeDays(serviceDTO.getBookBeforeDays());
    if (serviceDTO.getIsAvailable() != null) service.setIsAvailable(serviceDTO.getIsAvailable());
    if (serviceDTO.getServiceAreaType() != null) service.setServiceAreaType(serviceDTO.getServiceAreaType());
    if (serviceDTO.getCoverImageUrl() != null) service.setCoverImageUrl(serviceDTO.getCoverImageUrl());
    if (serviceDTO.getCancellationPolicy() != null) service.setCancellationPolicy(serviceDTO.getCancellationPolicy());

        com.example.WeddingVenderMngSystem.entity.Service savedService = serviceRepository.save(service);
    serviceDTO.setServiceId(savedService.getServiceId());
    // Ensure vendorId returned in response for client usage
    serviceDTO.setVendorId(vendor.getVenderId());
    // Populate response with persisted values (including defaults)
    serviceDTO.setStatus(savedService.getStatus());
    serviceDTO.setPricingModel(savedService.getPricingModel());
    serviceDTO.setAdvancePercentage(savedService.getAdvancePercentage());
    serviceDTO.setDiscountPercent(savedService.getDiscountPercent());
    serviceDTO.setBookBeforeDays(savedService.getBookBeforeDays());
    serviceDTO.setIsAvailable(savedService.getIsAvailable());
    serviceDTO.setServiceAreaType(savedService.getServiceAreaType());
    serviceDTO.setCoverImageUrl(savedService.getCoverImageUrl());
    serviceDTO.setCancellationPolicy(savedService.getCancellationPolicy());
    if (savedService.getCreatedAt() != null) serviceDTO.setCreatedAt(savedService.getCreatedAt().toString());
    if (savedService.getUpdatedAt() != null) serviceDTO.setUpdatedAt(savedService.getUpdatedAt().toString());
    serviceDTO.setIsDeleted(savedService.getIsDeleted());

    // Create admin notification for new service creation
    String title = "New Service Created: " + savedService.getName();
    String message = "Vendor " + vendor.getUser().getUsername() + " has created a new service: " + savedService.getName();
    adminNotificationService.createNotification(
        AdminNotificationType.SERVICE_CREATED,
        title,
        message,
        savedService.getServiceId(),
        NotificationPriority.NORMAL
    );

    return serviceDTO;
    }

    // Get all services
    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a service by ID
    public ServiceDTO getServiceById(Long id) {
        com.example.WeddingVenderMngSystem.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        return convertToDTO(service);
    }

    // Update a service
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
    com.example.WeddingVenderMngSystem.entity.Service service = serviceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Service not found"));

    // Update all fields from DTO
    service.setName(serviceDTO.getName());
    service.setDescription(serviceDTO.getDescription());
    service.setPricing(serviceDTO.getPricing());
    if (serviceDTO.getStatus() != null) service.setStatus(serviceDTO.getStatus());
    if (serviceDTO.getPricingModel() != null) service.setPricingModel(serviceDTO.getPricingModel());
    if (serviceDTO.getAdvancePercentage() != null) service.setAdvancePercentage(serviceDTO.getAdvancePercentage());
    if (serviceDTO.getDiscountPercent() != null) service.setDiscountPercent(serviceDTO.getDiscountPercent());
    if (serviceDTO.getBookBeforeDays() != null) service.setBookBeforeDays(serviceDTO.getBookBeforeDays());
    if (serviceDTO.getIsAvailable() != null) service.setIsAvailable(serviceDTO.getIsAvailable());
    if (serviceDTO.getServiceAreaType() != null) service.setServiceAreaType(serviceDTO.getServiceAreaType());
    if (serviceDTO.getCoverImageUrl() != null) service.setCoverImageUrl(serviceDTO.getCoverImageUrl());
    if (serviceDTO.getCancellationPolicy() != null) service.setCancellationPolicy(serviceDTO.getCancellationPolicy());
    if (serviceDTO.getIsDeleted() != null) service.setIsDeleted(serviceDTO.getIsDeleted());

    com.example.WeddingVenderMngSystem.entity.Service updatedService = serviceRepository.save(service);
    return convertToDTO(updatedService);
    }

    // Delete a service
    public void deleteService(Long id) {
    com.example.WeddingVenderMngSystem.entity.Service service = serviceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Service not found"));
    // Soft delete: mark as deleted
    if (Boolean.TRUE.equals(service.getIsDeleted())) {
        // Already deleted; no further action (idempotent)
        return;
    }
    service.setIsDeleted(true);
    serviceRepository.save(service);
    }

    // Convert entity to DTO
    private ServiceDTO convertToDTO(com.example.WeddingVenderMngSystem.entity.Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceId(service.getServiceId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPricing(service.getPricing());
        dto.setVendorId(service.getVendor().getVenderId());
        // Optionally include userId for client reference
        if (service.getVendor().getUser() != null) {
            dto.setUserId(service.getVendor().getUser().getUserId());
        }
    // Map extended fields
    dto.setStatus(service.getStatus());
    dto.setPricingModel(service.getPricingModel());
    dto.setAdvancePercentage(service.getAdvancePercentage());
    dto.setDiscountPercent(service.getDiscountPercent());
    dto.setBookBeforeDays(service.getBookBeforeDays());
    dto.setIsAvailable(service.getIsAvailable());
    dto.setServiceAreaType(service.getServiceAreaType());
    dto.setCoverImageUrl(service.getCoverImageUrl());
    dto.setCancellationPolicy(service.getCancellationPolicy());
    if (service.getCreatedAt() != null) dto.setCreatedAt(service.getCreatedAt().toString());
    if (service.getUpdatedAt() != null) dto.setUpdatedAt(service.getUpdatedAt().toString());
    dto.setIsDeleted(service.getIsDeleted());
    // Aggregated metrics (vendor-level)
    Long vendorId = service.getVendor().getVenderId();
    Long followerCount = followerRepository.countActiveFollowersByVendorId(vendorId);
    dto.setFollowerCount(followerCount);
    Long reviewCount = reviewRepository.countByVendorId(vendorId);
    dto.setReviewCount(reviewCount);
    Double avgRating = reviewRepository.averageRatingByVendorId(vendorId);
    dto.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : null); // round to 1 decimal
        return dto;
    }

    // Get Service by VenderID
    public List<ServiceDTO> GetServicesByVenderID(Long venderID){
    return serviceRepository.findByVendor_VenderIdAndIsDeletedFalse(venderID).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    // Get Services by UserID (resolve vendor via user)
    public List<ServiceDTO> getServicesByUserId(Long userId) {
        return vendorRepository.findByUser_UserId(userId)
                .map(vendor -> serviceRepository.findByVendor_VenderIdAndIsDeletedFalse(vendor.getVenderId()))
                .orElseThrow(() -> new RuntimeException("Vendor not found for userId: " + userId))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
