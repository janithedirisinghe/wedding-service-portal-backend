package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.entity.Service;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/{vendorId}/services")
    public List<Service> getServicesByVendor(@PathVariable Long vendorId) {
        return vendorService.getServicesByVendorId(vendorId);
    }

    @GetMapping("/getvendor/{vendorId}")
    public Vendor getVendorById(@PathVariable Long vendorId){
        Optional<Vendor> vendor = Optional.ofNullable(vendorService.getVendorById(vendorId));
        if(vendor.isPresent()){
            return ResponseEntity.ok(vendor.get()).getBody();
        }else {
            return (Vendor) ResponseEntity.notFound();
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
