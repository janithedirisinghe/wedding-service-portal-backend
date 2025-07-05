package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.PostItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostItemRepository extends JpaRepository<PostItem, Long> {
    List<PostItem> findByPost_PostId(Long postId);

}

