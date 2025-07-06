package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Find customer by user ID
    Optional<Customer> findByUser_UserId(Long userId);
    
    // Alternative query method if the above doesn't work
    @Query("SELECT c FROM Customer c WHERE c.user.userId = :userId")
    Optional<Customer> findByUserId(@Param("userId") Long userId);
}
