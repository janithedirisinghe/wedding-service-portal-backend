package com.example.WeddingVenderMngSystem.repository;

import com.example.WeddingVenderMngSystem.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post , Long> {
    List<Post> findByVendor_venderId(Long venderId);
    
    // Get random posts for timeline feed (using native query for better database compatibility)
    @Query(value = "SELECT * FROM posts ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Post> findRandomPostsNative(@Param("limit") int limit);
    
    // Get recent posts with pagination
    @Query("SELECT p FROM Post p ORDER BY p.postId DESC")
    Page<Post> findRandomPosts(Pageable pageable);
    
    // Get posts from vendors that a customer follows
    @Query("SELECT p FROM Post p WHERE p.vendor.venderId IN " +
           "(SELECT f.vendor.venderId FROM Follower f WHERE f.customer.customerId = :customerId AND f.isActive = true) " +
           "ORDER BY p.postId DESC")
    Page<Post> findPostsByFollowedVendors(@Param("customerId") Long customerId, Pageable pageable);
    
    // Get all posts ordered by most recent
    @Query("SELECT p FROM Post p ORDER BY p.postId DESC")
    Page<Post> findAllPostsOrderedByRecent(Pageable pageable);
    
    // Alternative method to get random posts using offset (for databases that don't support RAND())
    @Query("SELECT p FROM Post p ORDER BY p.postId")
    Page<Post> findPostsWithOffset(Pageable pageable);
}
