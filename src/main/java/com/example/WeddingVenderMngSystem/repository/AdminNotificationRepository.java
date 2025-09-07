package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.AdminNotification;
import com.example.WeddingVenderMngSystem.entity.AdminNotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {

    Page<AdminNotification> findByIsReadFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<AdminNotification> findByTypeOrderByCreatedAtDesc(AdminNotificationType type, Pageable pageable);

    long countByIsReadFalse();

    @Modifying
    @Query("UPDATE AdminNotification n SET n.isRead = true, n.readAt = :readAt WHERE n.isRead = false")
    int markAllAsRead(@Param("readAt") LocalDateTime readAt);

    @Modifying
    @Query("DELETE FROM AdminNotification n WHERE n.isRead = true AND n.createdAt < :cutoffDate")
    int deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
}
