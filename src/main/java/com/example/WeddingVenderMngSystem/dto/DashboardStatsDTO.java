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
public class DashboardStatsDTO {
    
    // Overview stats
    private OverviewStats overviewStats;
    
    // Revenue analytics
    private RevenueStats revenueStats;
    
    // Booking analytics
    private BookingStats bookingStats;
    
    // Vendor analytics
    private VendorStats vendorStats;
    
    // Customer analytics
    private CustomerStats customerStats;
    
    // Recent activities
    private List<RecentActivityDTO> recentActivities;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OverviewStats {
        private Long totalCustomers;
        private Long totalVendors;
        private Long totalBookings;
        private Long totalServices;
        private BigDecimal totalRevenue;
        private Long pendingVerifications;
        private Long activeBookings;
        private Double averageRating;

        public Long getTotalCustomers() {
            return totalCustomers;
        }

        public void setTotalCustomers(Long totalCustomers) {
            this.totalCustomers = totalCustomers;
        }

        public Long getTotalVendors() {
            return totalVendors;
        }

        public void setTotalVendors(Long totalVendors) {
            this.totalVendors = totalVendors;
        }

        public Long getTotalBookings() {
            return totalBookings;
        }

        public void setTotalBookings(Long totalBookings) {
            this.totalBookings = totalBookings;
        }

        public Long getTotalServices() {
            return totalServices;
        }

        public void setTotalServices(Long totalServices) {
            this.totalServices = totalServices;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public Long getPendingVerifications() {
            return pendingVerifications;
        }

        public void setPendingVerifications(Long pendingVerifications) {
            this.pendingVerifications = pendingVerifications;
        }

        public Long getActiveBookings() {
            return activeBookings;
        }

        public void setActiveBookings(Long activeBookings) {
            this.activeBookings = activeBookings;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueStats {
        private BigDecimal monthlyRevenue;
        private BigDecimal yearlyRevenue;
        private BigDecimal previousMonthRevenue;
        private Double revenueGrowthRate;
        private List<MonthlyRevenueData> monthlyRevenueChart;
        private Map<String, BigDecimal> revenueByServiceType;

        public BigDecimal getMonthlyRevenue() {
            return monthlyRevenue;
        }

        public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
            this.monthlyRevenue = monthlyRevenue;
        }

        public BigDecimal getYearlyRevenue() {
            return yearlyRevenue;
        }

        public void setYearlyRevenue(BigDecimal yearlyRevenue) {
            this.yearlyRevenue = yearlyRevenue;
        }

        public BigDecimal getPreviousMonthRevenue() {
            return previousMonthRevenue;
        }

        public void setPreviousMonthRevenue(BigDecimal previousMonthRevenue) {
            this.previousMonthRevenue = previousMonthRevenue;
        }

        public Double getRevenueGrowthRate() {
            return revenueGrowthRate;
        }

        public void setRevenueGrowthRate(Double revenueGrowthRate) {
            this.revenueGrowthRate = revenueGrowthRate;
        }

        public List<MonthlyRevenueData> getMonthlyRevenueChart() {
            return monthlyRevenueChart;
        }

        public void setMonthlyRevenueChart(List<MonthlyRevenueData> monthlyRevenueChart) {
            this.monthlyRevenueChart = monthlyRevenueChart;
        }

        public Map<String, BigDecimal> getRevenueByServiceType() {
            return revenueByServiceType;
        }

        public void setRevenueByServiceType(Map<String, BigDecimal> revenueByServiceType) {
            this.revenueByServiceType = revenueByServiceType;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingStats {
        private Long pendingBookings;
        private Long confirmedBookings;
        private Long completedBookings;
        private Long cancelledBookings;
        private Double bookingCompletionRate;
        private Map<String, Long> bookingsByStatus;
        private List<DailyBookingData> dailyBookingsChart;

        public Long getPendingBookings() {
            return pendingBookings;
        }

        public void setPendingBookings(Long pendingBookings) {
            this.pendingBookings = pendingBookings;
        }

        public Long getConfirmedBookings() {
            return confirmedBookings;
        }

        public void setConfirmedBookings(Long confirmedBookings) {
            this.confirmedBookings = confirmedBookings;
        }

        public Long getCompletedBookings() {
            return completedBookings;
        }

        public void setCompletedBookings(Long completedBookings) {
            this.completedBookings = completedBookings;
        }

        public Long getCancelledBookings() {
            return cancelledBookings;
        }

        public void setCancelledBookings(Long cancelledBookings) {
            this.cancelledBookings = cancelledBookings;
        }

        public Double getBookingCompletionRate() {
            return bookingCompletionRate;
        }

        public void setBookingCompletionRate(Double bookingCompletionRate) {
            this.bookingCompletionRate = bookingCompletionRate;
        }

        public Map<String, Long> getBookingsByStatus() {
            return bookingsByStatus;
        }

        public void setBookingsByStatus(Map<String, Long> bookingsByStatus) {
            this.bookingsByStatus = bookingsByStatus;
        }

        public List<DailyBookingData> getDailyBookingsChart() {
            return dailyBookingsChart;
        }

        public void setDailyBookingsChart(List<DailyBookingData> dailyBookingsChart) {
            this.dailyBookingsChart = dailyBookingsChart;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorStats {
        private Long totalVendors;
        private Long verifiedVendors;
        private Long pendingVerifications;
        private Long activeVendors;
        private Map<String, Long> vendorsByType;
        private Double averageVendorRating;
        private List<TopVendorDTO> topPerformingVendors;

        public Long getTotalVendors() {
            return totalVendors;
        }

        public void setTotalVendors(Long totalVendors) {
            this.totalVendors = totalVendors;
        }

        public Long getVerifiedVendors() {
            return verifiedVendors;
        }

        public void setVerifiedVendors(Long verifiedVendors) {
            this.verifiedVendors = verifiedVendors;
        }

        public Long getPendingVerifications() {
            return pendingVerifications;
        }

        public void setPendingVerifications(Long pendingVerifications) {
            this.pendingVerifications = pendingVerifications;
        }

        public Long getActiveVendors() {
            return activeVendors;
        }

        public void setActiveVendors(Long activeVendors) {
            this.activeVendors = activeVendors;
        }

        public Map<String, Long> getVendorsByType() {
            return vendorsByType;
        }

        public void setVendorsByType(Map<String, Long> vendorsByType) {
            this.vendorsByType = vendorsByType;
        }

        public Double getAverageVendorRating() {
            return averageVendorRating;
        }

        public void setAverageVendorRating(Double averageVendorRating) {
            this.averageVendorRating = averageVendorRating;
        }

        public List<TopVendorDTO> getTopPerformingVendors() {
            return topPerformingVendors;
        }

        public void setTopPerformingVendors(List<TopVendorDTO> topPerformingVendors) {
            this.topPerformingVendors = topPerformingVendors;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerStats {
        private Long totalCustomers;
        private Long activeCustomers;
        private Long newCustomersThisMonth;
        private Double customerRetentionRate;
        private List<CustomerGrowthData> customerGrowthChart;

        public Long getTotalCustomers() {
            return totalCustomers;
        }

        public void setTotalCustomers(Long totalCustomers) {
            this.totalCustomers = totalCustomers;
        }

        public Long getActiveCustomers() {
            return activeCustomers;
        }

        public void setActiveCustomers(Long activeCustomers) {
            this.activeCustomers = activeCustomers;
        }

        public Long getNewCustomersThisMonth() {
            return newCustomersThisMonth;
        }

        public void setNewCustomersThisMonth(Long newCustomersThisMonth) {
            this.newCustomersThisMonth = newCustomersThisMonth;
        }

        public Double getCustomerRetentionRate() {
            return customerRetentionRate;
        }

        public void setCustomerRetentionRate(Double customerRetentionRate) {
            this.customerRetentionRate = customerRetentionRate;
        }

        public List<CustomerGrowthData> getCustomerGrowthChart() {
            return customerGrowthChart;
        }

        public void setCustomerGrowthChart(List<CustomerGrowthData> customerGrowthChart) {
            this.customerGrowthChart = customerGrowthChart;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyRevenueData {
        private String month;
        private BigDecimal revenue;
        private Long bookingCount;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }

        public Long getBookingCount() {
            return bookingCount;
        }

        public void setBookingCount(Long bookingCount) {
            this.bookingCount = bookingCount;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyBookingData {
        private String date;
        private Long bookings;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Long getBookings() {
            return bookings;
        }

        public void setBookings(Long bookings) {
            this.bookings = bookings;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerGrowthData {
        private String month;
        private Long newCustomers;
        private Long totalCustomers;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Long getNewCustomers() {
            return newCustomers;
        }

        public void setNewCustomers(Long newCustomers) {
            this.newCustomers = newCustomers;
        }

        public Long getTotalCustomers() {
            return totalCustomers;
        }

        public void setTotalCustomers(Long totalCustomers) {
            this.totalCustomers = totalCustomers;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopVendorDTO {
        private Long vendorId;
        private String businessName;
        private String venType;
        private Double averageRating;
        private Long totalBookings;
        private BigDecimal totalRevenue;

        public Long getVendorId() {
            return vendorId;
        }

        public void setVendorId(Long vendorId) {
            this.vendorId = vendorId;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getVenType() {
            return venType;
        }

        public void setVenType(String venType) {
            this.venType = venType;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

        public Long getTotalBookings() {
            return totalBookings;
        }

        public void setTotalBookings(Long totalBookings) {
            this.totalBookings = totalBookings;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivityDTO {
        private String type; // BOOKING, PAYMENT, VENDOR_REGISTRATION, REVIEW
        private String description;
        private String timestamp;
        private String userType; // CUSTOMER, VENDOR
        private String userName;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public OverviewStats getOverviewStats() {
        return overviewStats;
    }

    public void setOverviewStats(OverviewStats overviewStats) {
        this.overviewStats = overviewStats;
    }

    public RevenueStats getRevenueStats() {
        return revenueStats;
    }

    public void setRevenueStats(RevenueStats revenueStats) {
        this.revenueStats = revenueStats;
    }

    public BookingStats getBookingStats() {
        return bookingStats;
    }

    public void setBookingStats(BookingStats bookingStats) {
        this.bookingStats = bookingStats;
    }

    public VendorStats getVendorStats() {
        return vendorStats;
    }

    public void setVendorStats(VendorStats vendorStats) {
        this.vendorStats = vendorStats;
    }

    public CustomerStats getCustomerStats() {
        return customerStats;
    }

    public void setCustomerStats(CustomerStats customerStats) {
        this.customerStats = customerStats;
    }

    public List<RecentActivityDTO> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<RecentActivityDTO> recentActivities) {
        this.recentActivities = recentActivities;
    }
}
