package com.example.WeddingVenderMngSystem.dto;

public class CustomerStatsDTO {
    private Long favoritesCount;
    private Long reviewsCount;
    private Long bookingsCount;

    public CustomerStatsDTO() {}

    public CustomerStatsDTO(Long favoritesCount, Long reviewsCount, Long bookingsCount) {
        this.favoritesCount = favoritesCount;
        this.reviewsCount = reviewsCount;
        this.bookingsCount = bookingsCount;
    }

    public Long getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Long favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public Long getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Long reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Long getBookingsCount() {
        return bookingsCount;
    }

    public void setBookingsCount(Long bookingsCount) {
        this.bookingsCount = bookingsCount;
    }
}
