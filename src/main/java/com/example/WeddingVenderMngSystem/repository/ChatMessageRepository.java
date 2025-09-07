package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    Page<ChatMessage> findByChatRoom_ChatRoomIdOrderBySentAtDesc(Long chatRoomId, Pageable pageable);
    
    List<ChatMessage> findByChatRoom_ChatRoomIdOrderBySentAtAsc(Long chatRoomId);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.chatRoomId = :chatRoomId ORDER BY cm.sentAt DESC")
    List<ChatMessage> findRecentMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
    
    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoom.chatRoomId = :chatRoomId AND cm.sender.userId != :userId AND cm.status != 'READ'")
    int countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.chatRoomId = :chatRoomId AND cm.sender.userId != :userId AND cm.status != 'READ'")
    List<ChatMessage> findUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
}
