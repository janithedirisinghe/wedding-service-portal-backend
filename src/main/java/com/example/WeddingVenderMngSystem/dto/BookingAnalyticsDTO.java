package com.example.WeddingVenderMngSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAnalyticsDTO {
    
    private BookingOverview overview;
    private List<BookingTrend> dailyTrends;
    private List<BookingTrend> monthlyTrends;
    private Map<String, BookingServiceStats> serviceTypeStats;
    private List<VendorBookingStats> topVendorsByBookings;
    private BookingPerformanceMetrics performance;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingOverview {
        private Long totalBookings;
        private Long pendingBookings;
        private Long confirmedBookings;
        private Long completedBookings;
        private Long cancelledBookings;
        private Double completionRate;
        private Double cancellationRate;
        private Double averageResponseTime; // in hours
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingTrend {
        private String period; // date or month
        private Long totalBookings;
        private Long pendingBookings;
        private Long confirmedBookings;
        private Long completedBookings;
        private Long cancelledBookings;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingServiceStats {
        private String serviceType;
        private Long totalBookings;
        private Long completedBookings;
        private Double completionRate;
        private Double averageRating;
        private Long uniqueCustomers;
        private Long activeVendors;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorBookingStats {
        private Long vendorId;
        private String businessName;
        private String serviceType;
        private Long totalBookings;
        private Long completedBookings;
        private Double completionRate;
        private Double averageResponseTime;
        private Double averageRating;
        private String location;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingPerformanceMetrics {
        private Double averageBookingValue;
        private Double peakBookingHour;
        private String mostPopularServiceType;
        private String mostActiveDay;
        private Long repeatCustomers;
        private Double customerRetentionRate;
        private LocalDateTime lastUpdated;
    }
}
