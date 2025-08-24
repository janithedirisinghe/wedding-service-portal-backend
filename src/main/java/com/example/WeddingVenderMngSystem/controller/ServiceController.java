package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.ServiceDTO;
import com.example.WeddingVenderMngSystem.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    // Create a new service
    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceService.createService(serviceDTO));
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping("getServiceByVendorId/{venderId}")
    public ResponseEntity<List<ServiceDTO>> getServicesByVenderId(@PathVariable Long venderId){
        return ResponseEntity.ok(serviceService.GetServicesByVenderID(venderId));
    }

    // New: Get services by userId (resolves vendor internally)
    @GetMapping("getServiceByUserId/{userId}")
    public ResponseEntity<List<ServiceDTO>> getServicesByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(serviceService.getServicesByUserId(userId));
    }

    // Update a service
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Long id, @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceService.updateService(id, serviceDTO));
    }

    // Delete a service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
