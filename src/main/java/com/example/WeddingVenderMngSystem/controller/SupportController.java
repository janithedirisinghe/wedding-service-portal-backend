package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.SupportDTO;
import com.example.WeddingVenderMngSystem.entity.Support;
import com.example.WeddingVenderMngSystem.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supports")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @GetMapping
    public ResponseEntity<List<SupportDTO>> getAllSupports() {
        return ResponseEntity.ok(supportService.getAllSupports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportDTO> getSupportById(@PathVariable Long id) {
        SupportDTO support = supportService.getSupportById(id);
        return support != null ? ResponseEntity.ok(support) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportDTO>> getSupportsByUserId(@PathVariable Long userId) {
        List<SupportDTO> supports = supportService.getSupportsByUserId(userId);
        return ResponseEntity.ok(supports);
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<SupportDTO>> getSupportsBySeverity(@PathVariable Support.Severity severity) {
        List<SupportDTO> supports = supportService.getSupportsBySeverity(severity);
        return ResponseEntity.ok(supports);
    }

    @PostMapping
    public ResponseEntity<SupportDTO> createSupport(@RequestBody SupportDTO supportDTO) {
        return ResponseEntity.ok(supportService.createSupport(supportDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportDTO> updateSupport(@PathVariable Long id, @RequestBody SupportDTO supportDTO) {
        SupportDTO updatedSupport = supportService.updateSupport(id, supportDTO);
        return updatedSupport != null ? ResponseEntity.ok(updatedSupport) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupport(@PathVariable Long id) {
        supportService.deleteSupport(id);
        return ResponseEntity.noContent().build();
    }
}
