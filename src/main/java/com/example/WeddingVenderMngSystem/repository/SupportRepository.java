package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {
    List<Support> findByUser_UserId(Long userId);
    List<Support> findBySeverity(Support.Severity severity);
}
