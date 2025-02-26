package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.ServiceDTO;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.ServiceRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private VendorRepository vendorRepository;

    // Create a new Service
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Vendor vendor = vendorRepository.findById(serviceDTO.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        com.example.WeddingVenderMngSystem.entity.Service service = new com.example.WeddingVenderMngSystem.entity.Service();
        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setPricing(serviceDTO.getPricing());
        service.setVendor(vendor);

        com.example.WeddingVenderMngSystem.entity.Service savedService = serviceRepository.save(service);
        serviceDTO.setServiceId(savedService.getServiceId());
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

        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setPricing(serviceDTO.getPricing());

        com.example.WeddingVenderMngSystem.entity.Service updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }

    // Delete a service
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        serviceRepository.deleteById(id);
    }

    // Convert entity to DTO
    private ServiceDTO convertToDTO(com.example.WeddingVenderMngSystem.entity.Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setServiceId(service.getServiceId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPricing(service.getPricing());
        dto.setVendorId(service.getVendor().getVenderId());
        return dto;
    }
}
