package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.VendorTypeDTO;
import com.example.WeddingVenderMngSystem.entity.VendorType;
import com.example.WeddingVenderMngSystem.repository.VendorTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VendorTypeService {

    @Autowired
    private VendorTypeRepository vendorTypeRepository;

    /**
     * Create a new vendor type
     */
    public VendorTypeDTO createVendorType(VendorTypeDTO vendorTypeDTO) {
        // Check if vendor type already exists (case insensitive)
        if (vendorTypeRepository.existsByVendorTypeNameIgnoreCase(vendorTypeDTO.getVendorTypeName())) {
            throw new IllegalArgumentException("Vendor type with name '" + vendorTypeDTO.getVendorTypeName() + "' already exists");
        }

        VendorType vendorType = new VendorType();
        vendorType.setVendorTypeName(vendorTypeDTO.getVendorTypeName());
        vendorType.setDescription(vendorTypeDTO.getDescription());
        vendorType.setIsActive(vendorTypeDTO.getIsActive() != null ? vendorTypeDTO.getIsActive() : true);

        VendorType savedVendorType = vendorTypeRepository.save(vendorType);
        return convertToDTO(savedVendorType);
    }

    /**
     * Get all vendor types
     */
    public List<VendorTypeDTO> getAllVendorTypes() {
        return vendorTypeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all active vendor types
     */
    public List<VendorTypeDTO> getAllActiveVendorTypes() {
        return vendorTypeRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get vendor type by ID
     */
    public VendorTypeDTO getVendorTypeById(Long vendorTypeId) {
        VendorType vendorType = vendorTypeRepository.findById(vendorTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with ID: " + vendorTypeId));
        return convertToDTO(vendorType);
    }

    /**
     * Get vendor type by name
     */
    public VendorTypeDTO getVendorTypeByName(String vendorTypeName) {
        VendorType vendorType = vendorTypeRepository.findByVendorTypeNameIgnoreCase(vendorTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with name: " + vendorTypeName));
        return convertToDTO(vendorType);
    }

    /**
     * Update vendor type
     */
    public VendorTypeDTO updateVendorType(Long vendorTypeId, VendorTypeDTO vendorTypeDTO) {
        VendorType existingVendorType = vendorTypeRepository.findById(vendorTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with ID: " + vendorTypeId));

        // Check if new name already exists (only if name is being changed)
        if (!existingVendorType.getVendorTypeName().equalsIgnoreCase(vendorTypeDTO.getVendorTypeName())) {
            if (vendorTypeRepository.existsByVendorTypeNameIgnoreCase(vendorTypeDTO.getVendorTypeName())) {
                throw new IllegalArgumentException("Vendor type with name '" + vendorTypeDTO.getVendorTypeName() + "' already exists");
            }
        }

        existingVendorType.setVendorTypeName(vendorTypeDTO.getVendorTypeName());
        existingVendorType.setDescription(vendorTypeDTO.getDescription());
        if (vendorTypeDTO.getIsActive() != null) {
            existingVendorType.setIsActive(vendorTypeDTO.getIsActive());
        }

        VendorType updatedVendorType = vendorTypeRepository.save(existingVendorType);
        return convertToDTO(updatedVendorType);
    }

    /**
     * Soft delete vendor type (set isActive to false)
     */
    public void softDeleteVendorType(Long vendorTypeId) {
        VendorType vendorType = vendorTypeRepository.findById(vendorTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with ID: " + vendorTypeId));
        
        vendorType.setIsActive(false);
        vendorTypeRepository.save(vendorType);
    }

    /**
     * Activate vendor type (set isActive to true)
     */
    public VendorTypeDTO activateVendorType(Long vendorTypeId) {
        VendorType vendorType = vendorTypeRepository.findById(vendorTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with ID: " + vendorTypeId));
        
        vendorType.setIsActive(true);
        VendorType activatedVendorType = vendorTypeRepository.save(vendorType);
        return convertToDTO(activatedVendorType);
    }

    /**
     * Hard delete vendor type (permanently remove from database)
     */
    public void deleteVendorType(Long vendorTypeId) {
        if (!vendorTypeRepository.existsById(vendorTypeId)) {
            throw new IllegalArgumentException("Vendor type not found with ID: " + vendorTypeId);
        }
        vendorTypeRepository.deleteById(vendorTypeId);
    }

    /**
     * Search vendor types by name (case insensitive, partial match)
     */
    public List<VendorTypeDTO> searchVendorTypesByName(String vendorTypeName) {
        return vendorTypeRepository.findByVendorTypeNameContainingIgnoreCase(vendorTypeName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search active vendor types by name (case insensitive, partial match)
     */
    public List<VendorTypeDTO> searchActiveVendorTypesByName(String vendorTypeName) {
        return vendorTypeRepository.findByVendorTypeNameContainingIgnoreCaseAndIsActiveTrue(vendorTypeName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if vendor type exists by name
     */
    public boolean vendorTypeExists(String vendorTypeName) {
        return vendorTypeRepository.existsByVendorTypeNameIgnoreCase(vendorTypeName);
    }

    /**
     * Get count of active vendor types
     */
    public long getActiveVendorTypesCount() {
        return vendorTypeRepository.countActiveVendorTypes();
    }

    /**
     * Get count of inactive vendor types
     */
    public long getInactiveVendorTypesCount() {
        return vendorTypeRepository.countInactiveVendorTypes();
    }

    /**
     * Convert entity to DTO
     */
    private VendorTypeDTO convertToDTO(VendorType vendorType) {
        VendorTypeDTO dto = new VendorTypeDTO();
        dto.setVendorTypeId(vendorType.getVendorTypeId());
        dto.setVendorTypeName(vendorType.getVendorTypeName());
        dto.setDescription(vendorType.getDescription());
        dto.setCreatedDate(vendorType.getCreatedDate());
        dto.setUpdatedDate(vendorType.getUpdatedDate());
        dto.setIsActive(vendorType.getIsActive());
        return dto;
    }
}
