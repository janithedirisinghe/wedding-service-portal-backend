package com.example.WeddingVenderMngSystem.service;


import com.example.WeddingVenderMngSystem.dto.PostDTO;
import com.example.WeddingVenderMngSystem.entity.Post;
import com.example.WeddingVenderMngSystem.entity.PostItem;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.PostItemRepository;
import com.example.WeddingVenderMngSystem.repository.PostRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PostItemRepository postItemRepository;

    @Autowired
    private SupabaseStorageService supabaseStorageService; // Make sure this service is available

    public PostDTO createPostWithImages(PostDTO postDTO, MultipartFile[] images) {

        try{
        Vendor vendor = vendorRepository.findById(postDTO.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // 1. Save the post
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setLocation(postDTO.getLocation());
        post.setDate(postDTO.getDate());
        post.setVendor(vendor);
        Post savedPost = postRepository.save(post);
        System.out.println("Received " + images.length + " images");
        // 2. Upload each image to Supabase and save PostItem
        List<String> uploadedUrls = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                System.out.println("Uploading: " + file.getOriginalFilename());
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String imageUrl = supabaseStorageService.uploadFile(file, fileName); // upload to Supabase

                PostItem postItem = new PostItem();
                postItem.setItemUrl(imageUrl);
                postItem.setPost(savedPost);
                postItemRepository.save(postItem);
                System.out.println("PostItem saved for: " + imageUrl);
                uploadedUrls.add(imageUrl);
            }
        }

        // 3. Prepare DTO with uploaded image URLs
        postDTO.setPostId(savedPost.getPostId());
        postDTO.setItemUrls(uploadedUrls);
        return postDTO;
    }
    catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create post with images: " + e.getMessage());
        }
    }

    public List<PostDTO> getPostsByVendorId(Long vendorId) {
        List<Post> posts = postRepository.findByVendor_venderId(vendorId);
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            dto.setPostId(post.getPostId());
            dto.setContent(post.getContent());
            dto.setLocation(post.getLocation());
            dto.setDate(post.getDate());
            dto.setVendorId(post.getVendor().getVenderId());

            // Fetch PostItem URLs
            List<PostItem> items = postItemRepository.findByPost_PostId(post.getPostId());
            List<String> itemUrls = new ArrayList<>();
            for (PostItem item : items) {
                itemUrls.add(item.getItemUrl());
            }

            dto.setItemUrls(itemUrls);
            postDTOs.add(dto);
        }

        return postDTOs;
    }





}
