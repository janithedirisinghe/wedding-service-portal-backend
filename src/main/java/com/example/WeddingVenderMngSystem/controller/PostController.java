package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.PostDTO;
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


}
