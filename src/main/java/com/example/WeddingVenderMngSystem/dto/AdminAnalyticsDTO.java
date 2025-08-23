package com.example.WeddingVenderMngSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAnalyticsDTO {
    
    // Platform metrics
    private PlatformMetrics platformMetrics;
    
    // Performance metrics
    private PerformanceMetrics performanceMetrics;
    
    // Financial metrics
    private FinancialMetrics financialMetrics;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatformMetrics {
        private Long totalUsers;
        private Long monthlyActiveUsers;
        private Long dailyActiveUsers;
        private Double userGrowthRate;
        private Double platformUtilizationRate;
        private Long totalTransactions;
        private LocalDateTime lastUpdated;

        public Long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public Long getMonthlyActiveUsers() {
            return monthlyActiveUsers;
        }

        public void setMonthlyActiveUsers(Long monthlyActiveUsers) {
            this.monthlyActiveUsers = monthlyActiveUsers;
        }

        public Long getDailyActiveUsers() {
            return dailyActiveUsers;
        }

        public void setDailyActiveUsers(Long dailyActiveUsers) {
            this.dailyActiveUsers = dailyActiveUsers;
        }

        public Double getUserGrowthRate() {
            return userGrowthRate;
        }

        public void setUserGrowthRate(Double userGrowthRate) {
            this.userGrowthRate = userGrowthRate;
        }

        public Double getPlatformUtilizationRate() {
            return platformUtilizationRate;
        }

        public void setPlatformUtilizationRate(Double platformUtilizationRate) {
            this.platformUtilizationRate = platformUtilizationRate;
        }

        public Long getTotalTransactions() {
            return totalTransactions;
        }

        public void setTotalTransactions(Long totalTransactions) {
            this.totalTransactions = totalTransactions;
        }

        public LocalDateTime getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceMetrics {
        private Double averageBookingResponseTime; // in hours
        private Double customerSatisfactionScore;
        private Double vendorSatisfactionScore;
        private Long disputeResolutionCount;
        private Double bookingSuccessRate;
        private Double paymentSuccessRate;

        public Double getAverageBookingResponseTime() {
            return averageBookingResponseTime;
        }

        public void setAverageBookingResponseTime(Double averageBookingResponseTime) {
            this.averageBookingResponseTime = averageBookingResponseTime;
        }

        public Double getCustomerSatisfactionScore() {
            return customerSatisfactionScore;
        }

        public void setCustomerSatisfactionScore(Double customerSatisfactionScore) {
            this.customerSatisfactionScore = customerSatisfactionScore;
        }

        public Double getVendorSatisfactionScore() {
            return vendorSatisfactionScore;
        }

        public void setVendorSatisfactionScore(Double vendorSatisfactionScore) {
            this.vendorSatisfactionScore = vendorSatisfactionScore;
        }

        public Long getDisputeResolutionCount() {
            return disputeResolutionCount;
        }

        public void setDisputeResolutionCount(Long disputeResolutionCount) {
            this.disputeResolutionCount = disputeResolutionCount;
        }

        public Double getBookingSuccessRate() {
            return bookingSuccessRate;
        }

        public void setBookingSuccessRate(Double bookingSuccessRate) {
            this.bookingSuccessRate = bookingSuccessRate;
        }

        public Double getPaymentSuccessRate() {
            return paymentSuccessRate;
        }

        public void setPaymentSuccessRate(Double paymentSuccessRate) {
            this.paymentSuccessRate = paymentSuccessRate;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialMetrics {
        private BigDecimal totalPlatformRevenue;
        private BigDecimal averageTransactionValue;
        private BigDecimal monthlyRecurringRevenue;
        private BigDecimal projectedAnnualRevenue;
        private Double revenuePerCustomer;
        private Double revenuePerVendor;

        public BigDecimal getTotalPlatformRevenue() {
            return totalPlatformRevenue;
        }

        public void setTotalPlatformRevenue(BigDecimal totalPlatformRevenue) {
            this.totalPlatformRevenue = totalPlatformRevenue;
        }

        public BigDecimal getAverageTransactionValue() {
            return averageTransactionValue;
        }

        public void setAverageTransactionValue(BigDecimal averageTransactionValue) {
            this.averageTransactionValue = averageTransactionValue;
        }

        public BigDecimal getMonthlyRecurringRevenue() {
            return monthlyRecurringRevenue;
        }

        public void setMonthlyRecurringRevenue(BigDecimal monthlyRecurringRevenue) {
            this.monthlyRecurringRevenue = monthlyRecurringRevenue;
        }

        public BigDecimal getProjectedAnnualRevenue() {
            return projectedAnnualRevenue;
        }

        public void setProjectedAnnualRevenue(BigDecimal projectedAnnualRevenue) {
            this.projectedAnnualRevenue = projectedAnnualRevenue;
        }

        public Double getRevenuePerCustomer() {
            return revenuePerCustomer;
        }

        public void setRevenuePerCustomer(Double revenuePerCustomer) {
            this.revenuePerCustomer = revenuePerCustomer;
        }

        public Double getRevenuePerVendor() {
            return revenuePerVendor;
        }

        public void setRevenuePerVendor(Double revenuePerVendor) {
            this.revenuePerVendor = revenuePerVendor;
        }
    }

    public PlatformMetrics getPlatformMetrics() {
        return platformMetrics;
    }

    public void setPlatformMetrics(PlatformMetrics platformMetrics) {
        this.platformMetrics = platformMetrics;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public FinancialMetrics getFinancialMetrics() {
        return financialMetrics;
    }

    public void setFinancialMetrics(FinancialMetrics financialMetrics) {
        this.financialMetrics = financialMetrics;
    }
}
