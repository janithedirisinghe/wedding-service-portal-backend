package com.example.WeddingVenderMngSystem.service;


import com.example.WeddingVenderMngSystem.dto.PostDTO;
import com.example.WeddingVenderMngSystem.dto.TimelinePostDTO;
import com.example.WeddingVenderMngSystem.entity.Customer;
import com.example.WeddingVenderMngSystem.entity.Post;
import com.example.WeddingVenderMngSystem.entity.PostItem;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.repository.CustomerRepository;
import com.example.WeddingVenderMngSystem.repository.FollowerRepository;
import com.example.WeddingVenderMngSystem.repository.PostItemRepository;
import com.example.WeddingVenderMngSystem.repository.PostRepository;
import com.example.WeddingVenderMngSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private FollowerRepository followerRepository;

    @Autowired
    private CustomerRepository customerRepository;

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
    
    // Timeline API methods - Facebook-like functionality
    public List<TimelinePostDTO> getTimelineFeed(Long userId, int page, int size) {
        Long customerId = null;
        
        if (userId != null) {
            // Convert userId to customerId
            Customer customer = customerRepository.findByUser_UserId(userId).orElse(null);
            customerId = customer != null ? customer.getCustomerId() : null;
        }
        
        if (customerId != null) {
            // Get posts from followed vendors first
            Pageable pageable = PageRequest.of(page, size);
            Page<Post> posts = postRepository.findPostsByFollowedVendors(customerId, pageable);
            
            // If there are fewer posts than requested, mix with random posts
            if (posts.getContent().size() < size) {
                List<Post> followedPosts = posts.getContent();
                int remainingSize = size - followedPosts.size();
                
                // Get random posts to fill the remaining slots
                List<Post> randomPosts = postRepository.findRandomPostsNative(remainingSize);
                
                // Combine the lists
                List<Post> mixedPosts = new ArrayList<>();
                mixedPosts.addAll(followedPosts);
                mixedPosts.addAll(randomPosts);
                
                return convertToTimelineDTO(mixedPosts, customerId);
            }
            
            return convertToTimelineDTO(posts.getContent(), customerId);
        } else {
            // Get random posts for guest users
            List<Post> posts = postRepository.findRandomPostsNative(size);
            return convertToTimelineDTO(posts, customerId);
        }
    }
    
    public List<TimelinePostDTO> getRandomPosts(int page, int size) {
        // For random posts, we'll use the native query to get truly random results
        List<Post> posts = postRepository.findRandomPostsNative(size);
        return convertToTimelineDTO(posts, null);
    }
    
    public List<TimelinePostDTO> getFollowedVendorsPosts(Long userId, int page, int size) {
        Long customerId = null;
        
        if (userId != null) {
            // Convert userId to customerId
            Customer customer = customerRepository.findByUser_UserId(userId).orElse(null);
            customerId = customer != null ? customer.getCustomerId() : null;
        }
        
        if (customerId != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Post> posts = postRepository.findPostsByFollowedVendors(customerId, pageable);
            return convertToTimelineDTO(posts.getContent(), customerId);
        } else {
            // If no customer found, return empty list
            return new ArrayList<>();
        }
    }
    
    public List<TimelinePostDTO> getRecentPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllPostsOrderedByRecent(pageable);
        return convertToTimelineDTO(posts.getContent(), null);
    }
    
    private List<TimelinePostDTO> convertToTimelineDTO(List<Post> posts, Long currentCustomerId) {
        List<TimelinePostDTO> timelinePosts = new ArrayList<>();
        
        for (Post post : posts) {
            TimelinePostDTO dto = new TimelinePostDTO();
            dto.setPostId(post.getPostId());
            dto.setContent(post.getContent());
            dto.setLocation(post.getLocation());
            dto.setDate(post.getDate());
            dto.setCreatedAt(post.getDate()); // Assuming date is creation time
            
            // Vendor information
            Vendor vendor = post.getVendor();
            dto.setVendorId(vendor.getVenderId());
            dto.setVendorName(vendor.getBusinessName());
            dto.setVendorEmail(vendor.getUser() != null ? vendor.getUser().getEmail() : "");
            dto.setVendorServiceType(vendor.getVenType());
            // Set profile image if available
            // dto.setVendorProfileImage(vendor.getProfileImage());
            
            // Fetch PostItem URLs
            List<PostItem> items = postItemRepository.findByPost_PostId(post.getPostId());
            List<String> itemUrls = new ArrayList<>();
            for (PostItem item : items) {
                itemUrls.add(item.getItemUrl());
            }
            dto.setItemUrls(itemUrls);
            
            timelinePosts.add(dto);
        }
        
        return timelinePosts;
    }
}
