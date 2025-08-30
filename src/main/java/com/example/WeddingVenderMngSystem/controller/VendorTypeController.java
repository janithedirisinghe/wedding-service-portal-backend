package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.VendorTypeDTO;
import com.example.WeddingVenderMngSystem.service.VendorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor-types")
@CrossOrigin(origins = "*")
public class VendorTypeController {

    @Autowired
    private VendorTypeService vendorTypeService;

    /**
     * Create a new vendor type
     */
    @PostMapping
    public ResponseEntity<?> createVendorType(@RequestBody VendorTypeDTO vendorTypeDTO) {
        try {
            VendorTypeDTO createdVendorType = vendorTypeService.createVendorType(vendorTypeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVendorType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to create vendor type"));
        }
    }

    /**
     * Get all vendor types
     */
    @GetMapping
    public ResponseEntity<?> getAllVendorTypes() {
        try {
            List<VendorTypeDTO> vendorTypes = vendorTypeService.getAllVendorTypes();
            return ResponseEntity.ok(vendorTypes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch vendor types"));
        }
    }

    /**
     * Get all active vendor types
     */
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveVendorTypes() {
        try {
            List<VendorTypeDTO> vendorTypes = vendorTypeService.getAllActiveVendorTypes();
            return ResponseEntity.ok(vendorTypes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch active vendor types"));
        }
    }

    /**
     * Get vendor type by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getVendorTypeById(@PathVariable Long id) {
        try {
            VendorTypeDTO vendorType = vendorTypeService.getVendorTypeById(id);
            return ResponseEntity.ok(vendorType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch vendor type"));
        }
    }

    /**
     * Get vendor type by name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getVendorTypeByName(@PathVariable String name) {
        try {
            VendorTypeDTO vendorType = vendorTypeService.getVendorTypeByName(name);
            return ResponseEntity.ok(vendorType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch vendor type"));
        }
    }

    /**
     * Update vendor type
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVendorType(@PathVariable Long id, @RequestBody VendorTypeDTO vendorTypeDTO) {
        try {
            VendorTypeDTO updatedVendorType = vendorTypeService.updateVendorType(id, vendorTypeDTO);
            return ResponseEntity.ok(updatedVendorType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to update vendor type"));
        }
    }

    /**
     * Soft delete vendor type (deactivate)
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateVendorType(@PathVariable Long id) {
        try {
            vendorTypeService.softDeleteVendorType(id);
            return ResponseEntity.ok(Map.of("message", "Vendor type deactivated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to deactivate vendor type"));
        }
    }

    /**
     * Activate vendor type
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateVendorType(@PathVariable Long id) {
        try {
            VendorTypeDTO activatedVendorType = vendorTypeService.activateVendorType(id);
            return ResponseEntity.ok(activatedVendorType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to activate vendor type"));
        }
    }

    /**
     * Hard delete vendor type (permanently remove)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVendorType(@PathVariable Long id) {
        try {
            vendorTypeService.deleteVendorType(id);
            return ResponseEntity.ok(Map.of("message", "Vendor type deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete vendor type"));
        }
    }

    /**
     * Search vendor types by name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchVendorTypes(@RequestParam String name) {
        try {
            List<VendorTypeDTO> vendorTypes = vendorTypeService.searchVendorTypesByName(name);
            return ResponseEntity.ok(vendorTypes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to search vendor types"));
        }
    }

    /**
     * Search active vendor types by name
     */
    @GetMapping("/search/active")
    public ResponseEntity<?> searchActiveVendorTypes(@RequestParam String name) {
        try {
            List<VendorTypeDTO> vendorTypes = vendorTypeService.searchActiveVendorTypesByName(name);
            return ResponseEntity.ok(vendorTypes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to search active vendor types"));
        }
    }

    /**
     * Check if vendor type exists
     */
    @GetMapping("/exists/{name}")
    public ResponseEntity<?> checkVendorTypeExists(@PathVariable String name) {
        try {
            boolean exists = vendorTypeService.vendorTypeExists(name);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to check vendor type existence"));
        }
    }

    /**
     * Get vendor type statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getVendorTypeStats() {
        try {
            long activeCount = vendorTypeService.getActiveVendorTypesCount();
            long inactiveCount = vendorTypeService.getInactiveVendorTypesCount();
            long totalCount = activeCount + inactiveCount;
            
            Map<String, Object> stats = Map.of(
                "totalCount", totalCount,
                "activeCount", activeCount,
                "inactiveCount", inactiveCount
            );
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to fetch vendor type statistics"));
        }
    }
}
