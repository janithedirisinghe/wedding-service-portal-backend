package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Notification;
import com.example.WeddingVenderMngSystem.entity.NotificationType;
import com.example.WeddingVenderMngSystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Find all notifications for a user, ordered by creation date (newest first)
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    // Find unread notifications for a user
    Page<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user, Pageable pageable);
    
    // Count unread notifications for a user
    long countByUserAndIsReadFalse(User user);
    
    // Find notifications by type for a user
    Page<Notification> findByUserAndTypeOrderByCreatedAtDesc(User user, NotificationType type, Pageable pageable);
    
    // Find notifications by user and read status
    List<Notification> findByUserAndIsRead(User user, boolean isRead);
    
    // Mark all notifications as read for a user
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user = :user AND n.isRead = false")
    int markAllAsReadForUser(@Param("user") User user, @Param("readAt") LocalDateTime readAt);
    
    // Delete old read notifications (older than specified days)
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.isRead = true AND n.readAt < :cutoffDate")
    int deleteOldReadNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Find notifications related to a specific entity
    List<Notification> findByRelatedEntityIdAndType(Long relatedEntityId, NotificationType type);
    
    // Find recent notifications for a user (within last N days)
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.createdAt >= :fromDate ORDER BY n.createdAt DESC")
    List<Notification> findRecentNotifications(@Param("user") User user, @Param("fromDate") LocalDateTime fromDate);
}
