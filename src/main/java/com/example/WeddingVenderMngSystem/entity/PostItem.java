package com.example.WeddingVenderMngSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "postItems")
@Getter
@Setter
public class PostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postItemId;

    private String itemUrl;

    @ManyToOne
    @JoinColumn(name = "postId")
    @JsonBackReference
    private Post post;

    public Long getPostItemId() {
        return postItemId;
    }

    public void setPostItemId(Long postItemId) {
        this.postItemId = postItemId;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
