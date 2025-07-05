package com.example.WeddingVenderMngSystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDTO {

    private Long postId;

    private String content;

    private String location;

    private String date;

    private Long vendorId;

    private List<String> itemUrls;

    public Long getPostId() {
        return postId;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public String getContent() {
        return content;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getItemUrls() {
        return itemUrls;
    }

    public void setItemUrls(List<String> itemUrls) {
        this.itemUrls = itemUrls;
    }
}
