package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    Optional<ChatRoom> findByCustomer_CustomerIdAndVendor_VenderId(Long customerId, Long vendorId);
    
    Optional<ChatRoom> findByRoomName(String roomName);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.customer.customerId = :customerId ORDER BY cr.lastMessageAt DESC")
    List<ChatRoom> findByCustomerIdOrderByLastMessageAtDesc(@Param("customerId") Long customerId);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.vendor.venderId = :vendorId ORDER BY cr.lastMessageAt DESC")
    List<ChatRoom> findByVendorIdOrderByLastMessageAtDesc(@Param("vendorId") Long vendorId);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.customer.customerId = :userId OR cr.vendor.user.userId = :userId) ORDER BY cr.lastMessageAt DESC")
    List<ChatRoom> findByUserIdOrderByLastMessageAtDesc(@Param("userId") Long userId);
}
