package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.PostDTO;
import com.example.WeddingVenderMngSystem.dto.TimelinePostDTO;
import com.example.WeddingVenderMngSystem.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PostDTO> createPost(
            @RequestPart("post") PostDTO postDto,
            @RequestPart("images") MultipartFile[] images
    ) {
        try {
            PostDTO createdPost = postService.createPostWithImages(postDto, images);
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<PostDTO>> getPostsByVendor(@PathVariable Long vendorId) {
        try {
            List<PostDTO> posts = postService.getPostsByVendorId(vendorId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    // Timeline API endpoints - Facebook-like functionality
    
    @GetMapping("/timeline")
    public ResponseEntity<List<TimelinePostDTO>> getTimelineFeed(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<TimelinePostDTO> posts = postService.getTimelineFeed(userId, page, size);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/timeline/random")
    public ResponseEntity<List<TimelinePostDTO>> getRandomPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<TimelinePostDTO> posts = postService.getRandomPosts(page, size);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/timeline/following/{userId}")
    public ResponseEntity<List<TimelinePostDTO>> getFollowedVendorsPosts(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<TimelinePostDTO> posts = postService.getFollowedVendorsPosts(userId, page, size);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/timeline/recent")
    public ResponseEntity<List<TimelinePostDTO>> getRecentPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<TimelinePostDTO> posts = postService.getRecentPosts(page, size);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/timeline/explore")
    public ResponseEntity<List<TimelinePostDTO>> getExplorePosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        try {
            // For explore page, show random posts with larger page size
            List<TimelinePostDTO> posts = postService.getRandomPosts(page, size);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
