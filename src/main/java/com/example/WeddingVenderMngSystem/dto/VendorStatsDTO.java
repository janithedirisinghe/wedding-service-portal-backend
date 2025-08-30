package com.example.WeddingVenderMngSystem.dto;

public class VendorStatsDTO {
    private Long reviewCount;
    private Long postCount;
    private Long followerCount;

    public VendorStatsDTO() {}

    public VendorStatsDTO(Long reviewCount, Long postCount, Long followerCount) {
        this.reviewCount = reviewCount;
        this.postCount = postCount;
        this.followerCount = followerCount;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
    }
}
