package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.VendorUpdateDTO;
import com.example.WeddingVenderMngSystem.entity.Service;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/{userId}/services")
    public List<Service> getServicesByUser(@PathVariable Long userId) {
        return vendorService.getServicesByUserId(userId);
    }

    @GetMapping("/getvendor/{userId}")
    public Vendor getVendorByUserId(@PathVariable Long userId){
        Optional<Vendor> vendor = Optional.ofNullable(vendorService.getVendorByUserId(userId));
        if(vendor.isPresent()){
            return ResponseEntity.ok(vendor.get()).getBody();
        }else {
            return (Vendor) ResponseEntity.notFound();
        }

    }

    @PutMapping(value = "/updateprofile/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Vendor> updateVendorProfile(
            @PathVariable Long userId,
            @RequestPart("vendor") VendorUpdateDTO vendorUpdateDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            Vendor updatedVendor;
            if (profileImage != null && !profileImage.isEmpty()) {
                updatedVendor = vendorService.updateVendorProfileWithImage(userId, vendorUpdateDTO, profileImage);
            } else {
                updatedVendor = vendorService.updateVendorProfile(userId, vendorUpdateDTO);
            }
            return ResponseEntity.ok(updatedVendor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // New API: Get vendor by venderId
    @GetMapping("/getvenderByVenderId/{venderId}")
    public ResponseEntity<Vendor> getVendorByVenderId(@PathVariable Long venderId) {
        try {
            Vendor vendor = vendorService.getVendorById(venderId);
            return ResponseEntity.ok(vendor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


//    @GetMapping("/{vendorId}")
//    public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long vendorId) {
//        Vendor vendor = vendorService.getVendorById(vendorId);
//
//        if (vendor == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Convert the List<Service> to List<ServiceDTO>
//        List<ServiceDTO> serviceDTOs = vendor.getServices().stream()
//                .map(this::convertToServiceDTO)
//                .collect(Collectors.toList());
//
//        // Create VendorDTO using the constructor that takes all fields
//        VendorDTO vendorDTO = new VendorDTO(
//                vendor.getVenderId(),
//                vendor.getBusinessName(),
//                vendor.getAvailability(),
//                vendor.getLocation(),
//                vendor.getBRN(),
//                vendor.getCountry(),
//                vendor.getVenType(),
//                vendor.getBio(),
//                vendor.getTelNo(),
//                vendor.getUser().getUsername(),
//                vendor.getUser().getEmail(),
//                serviceDTOs
//        );
//
//        return ResponseEntity.ok(vendorDTO);
//    }


}
