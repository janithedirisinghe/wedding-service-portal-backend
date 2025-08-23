package com.example.WeddingVenderMngSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueAnalyticsDTO {
    
    private RevenueOverview overview;
    private List<MonthlyTrend> monthlyTrends;
    private List<ServiceTypeRevenue> revenueByServiceType;
    private List<VendorRevenue> topRevenueVendors;
    private PaymentAnalytics paymentAnalytics;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueOverview {
        private BigDecimal totalRevenue;
        private BigDecimal monthlyRevenue;
        private BigDecimal yearlyRevenue;
        private Double monthOverMonthGrowth;
        private Double yearOverYearGrowth;
        private BigDecimal averageTransactionValue;
        private BigDecimal projectedMonthlyRevenue;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrend {
        private String month;
        private BigDecimal revenue;
        private Long transactionCount;
        private BigDecimal averageValue;
        private Double growthRate;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceTypeRevenue {
        private String serviceType;
        private BigDecimal revenue;
        private Long bookingCount;
        private BigDecimal averageValue;
        private Double percentage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorRevenue {
        private Long vendorId;
        private String businessName;
        private String serviceType;
        private BigDecimal revenue;
        private Long bookingCount;
        private Double averageRating;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentAnalytics {
        private Long totalPayments;
        private Long successfulPayments;
        private Long failedPayments;
        private Long pendingPayments;
        private Double successRate;
        private BigDecimal totalFailedAmount;
        private Map<String, Long> paymentStatusDistribution;
    }
}
