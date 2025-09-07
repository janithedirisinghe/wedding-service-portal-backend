package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByVendor_VenderId(Long vendorId);
    // Only non-deleted services
    List<Service> findByVendor_VenderIdAndIsDeletedFalse(Long vendorId);
}
