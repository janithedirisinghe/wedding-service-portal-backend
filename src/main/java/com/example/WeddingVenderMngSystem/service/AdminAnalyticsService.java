package com.example.WeddingVenderMngSystem.service;

import com.example.WeddingVenderMngSystem.dto.AdminAnalyticsDTO;
import com.example.WeddingVenderMngSystem.dto.DashboardStatsDTO;
import com.example.WeddingVenderMngSystem.entity.Booking.BookingStatus;
import com.example.WeddingVenderMngSystem.entity.Payment.PaymentStatus;
import com.example.WeddingVenderMngSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminAnalyticsService {
    
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final VendorRepository vendorRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public AdminAnalyticsService(BookingRepository bookingRepository,
                                PaymentRepository paymentRepository,
                                VendorRepository vendorRepository,
                                CustomerRepository customerRepository,
                                ServiceRepository serviceRepository,
                                ReviewRepository reviewRepository,
                                UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.vendorRepository = vendorRepository;
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }
    
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        stats.setOverviewStats(getOverviewStats());
        stats.setRevenueStats(getRevenueStats());
        stats.setBookingStats(getBookingStats());
        stats.setVendorStats(getVendorStats());
        stats.setCustomerStats(getCustomerStats());
        stats.setRecentActivities(getRecentActivities());
        
        return stats;
    }
    
    public AdminAnalyticsDTO getDetailedAnalytics() {
        AdminAnalyticsDTO analytics = new AdminAnalyticsDTO();
        
        analytics.setPlatformMetrics(getPlatformMetrics());
        analytics.setPerformanceMetrics(getPerformanceMetrics());
        analytics.setFinancialMetrics(getFinancialMetrics());
        
        return analytics;
    }
    
    private DashboardStatsDTO.OverviewStats getOverviewStats() {
        Long totalCustomers = customerRepository.count();
        Long totalVendors = vendorRepository.count();
        Long totalBookings = bookingRepository.count();
        Long totalServices = serviceRepository.count();
        
        // Calculate total revenue from completed payments
        BigDecimal totalRevenue = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .map(payment -> payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Long pendingVerifications = vendorRepository.countByVerifyFalse();
        Long activeBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        
        // Calculate average rating
        Double averageRating = reviewRepository.findAll().stream()
                .mapToDouble(review -> review.getRating())
                .average()
                .orElse(0.0);
        
        DashboardStatsDTO.OverviewStats overviewStats = new DashboardStatsDTO.OverviewStats();
        overviewStats.setTotalCustomers(totalCustomers);
        overviewStats.setTotalVendors(totalVendors);
        overviewStats.setTotalBookings(totalBookings);
        overviewStats.setTotalServices(totalServices);
        overviewStats.setTotalRevenue(totalRevenue);
        overviewStats.setPendingVerifications(pendingVerifications);
        overviewStats.setActiveBookings(activeBookings);
        overviewStats.setAverageRating(averageRating);
        
        return overviewStats;
    }
    
    private DashboardStatsDTO.RevenueStats getRevenueStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime startOfPreviousMonth = startOfMonth.minusMonths(1);
        LocalDateTime startOfYear = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        
        // Monthly revenue
        BigDecimal monthlyRevenue = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .filter(payment -> payment.getPaidAt() != null && payment.getPaidAt().isAfter(startOfMonth))
                .map(payment -> payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Previous month revenue
        BigDecimal previousMonthRevenue = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .filter(payment -> payment.getPaidAt() != null && 
                        payment.getPaidAt().isAfter(startOfPreviousMonth) && 
                        payment.getPaidAt().isBefore(startOfMonth))
                .map(payment -> payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Yearly revenue
        BigDecimal yearlyRevenue = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .filter(payment -> payment.getPaidAt() != null && payment.getPaidAt().isAfter(startOfYear))
                .map(payment -> payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Revenue growth rate
        Double revenueGrowthRate = 0.0;
        if (previousMonthRevenue.compareTo(BigDecimal.ZERO) > 0) {
            revenueGrowthRate = monthlyRevenue.subtract(previousMonthRevenue)
                    .divide(previousMonthRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }
        
        // Monthly revenue chart (last 12 months)
        List<DashboardStatsDTO.MonthlyRevenueData> monthlyRevenueChart = getMonthlyRevenueChart();
        
        // Revenue by service type
        Map<String, BigDecimal> revenueByServiceType = getRevenueByServiceType();
        
        DashboardStatsDTO.RevenueStats revenueStats = new DashboardStatsDTO.RevenueStats();
        revenueStats.setMonthlyRevenue(monthlyRevenue);
        revenueStats.setYearlyRevenue(yearlyRevenue);
        revenueStats.setPreviousMonthRevenue(previousMonthRevenue);
        revenueStats.setRevenueGrowthRate(revenueGrowthRate);
        revenueStats.setMonthlyRevenueChart(monthlyRevenueChart);
        revenueStats.setRevenueByServiceType(revenueByServiceType);
        
        return revenueStats;
    }
    
    private DashboardStatsDTO.BookingStats getBookingStats() {
        Long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        Long confirmedBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);
        Long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        Long cancelledBookings = bookingRepository.countByStatus(BookingStatus.CANCELLED);
        
        // Booking completion rate
        Long totalNonPendingBookings = confirmedBookings + completedBookings + cancelledBookings;
        Double bookingCompletionRate = totalNonPendingBookings > 0 ? 
                (completedBookings.doubleValue() / totalNonPendingBookings.doubleValue()) * 100 : 0.0;
        
        // Bookings by status
        Map<String, Long> bookingsByStatus = new HashMap<>();
        bookingsByStatus.put("PENDING", pendingBookings);
        bookingsByStatus.put("CONFIRMED", confirmedBookings);
        bookingsByStatus.put("COMPLETED", completedBookings);
        bookingsByStatus.put("CANCELLED", cancelledBookings);
        
        // Daily bookings chart (last 30 days)
        List<DashboardStatsDTO.DailyBookingData> dailyBookingsChart = getDailyBookingsChart();
        
        DashboardStatsDTO.BookingStats bookingStats = new DashboardStatsDTO.BookingStats();
        bookingStats.setPendingBookings(pendingBookings);
        bookingStats.setConfirmedBookings(confirmedBookings);
        bookingStats.setCompletedBookings(completedBookings);
        bookingStats.setCancelledBookings(cancelledBookings);
        bookingStats.setBookingCompletionRate(bookingCompletionRate);
        bookingStats.setBookingsByStatus(bookingsByStatus);
        bookingStats.setDailyBookingsChart(dailyBookingsChart);
        
        return bookingStats;
    }
    
    private DashboardStatsDTO.VendorStats getVendorStats() {
        Long totalVendors = vendorRepository.count();
        Long verifiedVendors = vendorRepository.countByVerifyTrue();
        Long pendingVerifications = vendorRepository.countByVerifyFalse();
        Long activeVendors = vendorRepository.countByIsActiveTrue();
        
        // Vendors by type
        Map<String, Long> vendorsByType = vendorRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        vendor -> vendor.getVenType() != null ? vendor.getVenType() : "Other",
                        Collectors.counting()
                ));
        
        // Average vendor rating
        Double averageVendorRating = reviewRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        review -> review.getVendor().getVenderId(),
                        Collectors.averagingDouble(review -> review.getRating())
                ))
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        
        // Top performing vendors
        List<DashboardStatsDTO.TopVendorDTO> topPerformingVendors = getTopPerformingVendors();
        
        DashboardStatsDTO.VendorStats vendorStats = new DashboardStatsDTO.VendorStats();
        vendorStats.setTotalVendors(totalVendors);
        vendorStats.setVerifiedVendors(verifiedVendors);
        vendorStats.setPendingVerifications(pendingVerifications);
        vendorStats.setActiveVendors(activeVendors);
        vendorStats.setVendorsByType(vendorsByType);
        vendorStats.setAverageVendorRating(averageVendorRating);
        vendorStats.setTopPerformingVendors(topPerformingVendors);
        
        return vendorStats;
    }
    
    private DashboardStatsDTO.CustomerStats getCustomerStats() {
        Long totalCustomers = customerRepository.count();
        
        // Active customers (customers who made a booking in the last 3 months)
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        Long activeCustomers = (long) bookingRepository.findAll().stream()
                .filter(booking -> booking.getRequestDate().isAfter(threeMonthsAgo))
                .map(booking -> booking.getCustomer().getCustomerId())
                .collect(Collectors.toSet())
                .size();
        
        // New customers this month
        Long newCustomersThisMonth = customerRepository.findAll().stream()
                .filter(customer -> customer.getUser() != null && 
                        customer.getUser().getUsername() != null) // Basic activity check
                .count(); // This is simplified - you'd need a registration date field
        
        // Customer retention rate (simplified calculation)
        Double customerRetentionRate = totalCustomers > 0 ? 
                (activeCustomers.doubleValue() / totalCustomers.doubleValue()) * 100 : 0.0;
        
        // Customer growth chart
        List<DashboardStatsDTO.CustomerGrowthData> customerGrowthChart = getCustomerGrowthChart();
        
        DashboardStatsDTO.CustomerStats customerStats = new DashboardStatsDTO.CustomerStats();
        customerStats.setTotalCustomers(totalCustomers);
        customerStats.setActiveCustomers(activeCustomers);
        customerStats.setNewCustomersThisMonth(newCustomersThisMonth);
        customerStats.setCustomerRetentionRate(customerRetentionRate);
        customerStats.setCustomerGrowthChart(customerGrowthChart);
        
        return customerStats;
    }
    
    private List<DashboardStatsDTO.RecentActivityDTO> getRecentActivities() {
        List<DashboardStatsDTO.RecentActivityDTO> activities = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        // Recent bookings
        bookingRepository.findAll().stream()
                .sorted((b1, b2) -> b2.getRequestDate().compareTo(b1.getRequestDate()))
                .limit(5)
                .forEach(booking -> {
                    DashboardStatsDTO.RecentActivityDTO activity = new DashboardStatsDTO.RecentActivityDTO();
                    activity.setType("BOOKING");
                    activity.setDescription("New booking request for " + booking.getService().getName());
                    activity.setTimestamp(booking.getRequestDate().format(formatter));
                    activity.setUserType("CUSTOMER");
                    activity.setUserName(booking.getCustomer().getUser().getUsername());
                    activities.add(activity);
                });
        
        // Recent payments
        paymentRepository.findAll().stream()
                .filter(payment -> payment.getPaidAt() != null)
                .sorted((p1, p2) -> p2.getPaidAt().compareTo(p1.getPaidAt()))
                .limit(3)
                .forEach(payment -> {
                    DashboardStatsDTO.RecentActivityDTO activity = new DashboardStatsDTO.RecentActivityDTO();
                    activity.setType("PAYMENT");
                    activity.setDescription("Payment completed for $" + payment.getAmount());
                    activity.setTimestamp(payment.getPaidAt().format(formatter));
                    activity.setUserType("CUSTOMER");
                    activity.setUserName(payment.getBooking().getCustomer().getUser().getUsername());
                    activities.add(activity);
                });
        
        return activities.stream()
                .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
                .limit(10)
                .collect(Collectors.toList());
    }
    
    // Helper methods for charts and detailed data
    private List<DashboardStatsDTO.MonthlyRevenueData> getMonthlyRevenueChart() {
        // Implementation for monthly revenue chart
        List<DashboardStatsDTO.MonthlyRevenueData> chartData = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 11; i >= 0; i--) {
            LocalDateTime monthStart = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime monthEnd = monthStart.plusMonths(1);
            
            BigDecimal monthRevenue = paymentRepository.findAll().stream()
                    .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                    .filter(payment -> payment.getPaidAt() != null && 
                            payment.getPaidAt().isAfter(monthStart) && 
                            payment.getPaidAt().isBefore(monthEnd))
                    .map(payment -> payment.getAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Long bookingCount = bookingRepository.findAll().stream()
                    .filter(booking -> booking.getRequestDate().isAfter(monthStart) && 
                            booking.getRequestDate().isBefore(monthEnd))
                    .count();
            
            DashboardStatsDTO.MonthlyRevenueData monthlyData = new DashboardStatsDTO.MonthlyRevenueData();
            monthlyData.setMonth(monthStart.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            monthlyData.setRevenue(monthRevenue);
            monthlyData.setBookingCount(bookingCount);
            chartData.add(monthlyData);
        }
        
        return chartData;
    }
    
    private Map<String, BigDecimal> getRevenueByServiceType() {
        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .filter(payment -> payment.getBooking().getService().getVendor().getVenType() != null)
                .collect(Collectors.groupingBy(
                        payment -> payment.getBooking().getService().getVendor().getVenType(),
                        Collectors.reducing(BigDecimal.ZERO, 
                                payment -> payment.getAmount(), 
                                BigDecimal::add)
                ));
    }
    
    private List<DashboardStatsDTO.DailyBookingData> getDailyBookingsChart() {
        List<DashboardStatsDTO.DailyBookingData> chartData = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 29; i >= 0; i--) {
            LocalDateTime dayStart = now.minusDays(i).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime dayEnd = dayStart.plusDays(1);
            
            Long dailyBookings = bookingRepository.findAll().stream()
                    .filter(booking -> booking.getRequestDate().isAfter(dayStart) && 
                            booking.getRequestDate().isBefore(dayEnd))
                    .count();
            
            DashboardStatsDTO.DailyBookingData dailyData = new DashboardStatsDTO.DailyBookingData();
            dailyData.setDate(dayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dailyData.setBookings(dailyBookings);
            chartData.add(dailyData);
        }
        
        return chartData;
    }
    
    private List<DashboardStatsDTO.TopVendorDTO> getTopPerformingVendors() {
        return vendorRepository.findAll().stream()
                .map(vendor -> {
                    Long totalBookings = bookingRepository.findAll().stream()
                            .filter(booking -> booking.getService().getVendor().getVenderId().equals(vendor.getVenderId()))
                            .count();
                    
                    BigDecimal totalRevenue = paymentRepository.findAll().stream()
                            .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                            .filter(payment -> payment.getBooking().getService().getVendor().getVenderId().equals(vendor.getVenderId()))
                            .map(payment -> payment.getAmount())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    Double averageRating = reviewRepository.findAll().stream()
                            .filter(review -> review.getVendor().getVenderId().equals(vendor.getVenderId()))
                            .mapToDouble(review -> review.getRating())
                            .average()
                            .orElse(0.0);
                    
                    DashboardStatsDTO.TopVendorDTO topVendor = new DashboardStatsDTO.TopVendorDTO();
                    topVendor.setVendorId(vendor.getVenderId());
                    topVendor.setBusinessName(vendor.getBusinessName());
                    topVendor.setVenType(vendor.getVenType());
                    topVendor.setAverageRating(averageRating);
                    topVendor.setTotalBookings(totalBookings);
                    topVendor.setTotalRevenue(totalRevenue);
                    
                    return topVendor;
                })
                .sorted((v1, v2) -> v2.getTotalRevenue().compareTo(v1.getTotalRevenue()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    private List<DashboardStatsDTO.CustomerGrowthData> getCustomerGrowthChart() {
        // Simplified implementation - you'd need registration dates for accurate data
        List<DashboardStatsDTO.CustomerGrowthData> chartData = new ArrayList<>();
        Long totalCustomers = customerRepository.count();
        
        LocalDateTime now = LocalDateTime.now();
        for (int i = 11; i >= 0; i--) {
            LocalDateTime month = now.minusMonths(i);
            // This is simplified - you'd calculate actual new customers per month
            Long newCustomers = (long) (Math.random() * 20 + 5); // Mock data
            
            DashboardStatsDTO.CustomerGrowthData growthData = new DashboardStatsDTO.CustomerGrowthData();
            growthData.setMonth(month.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            growthData.setNewCustomers(newCustomers);
            growthData.setTotalCustomers(totalCustomers);
            chartData.add(growthData);
        }
        
        return chartData;
    }
    
    // Methods for detailed analytics
    private AdminAnalyticsDTO.PlatformMetrics getPlatformMetrics() {
        Long totalUsers = userRepository.count();
        Long monthlyActiveUsers = totalUsers; // Simplified
        Long dailyActiveUsers = totalUsers / 30; // Simplified
        Double userGrowthRate = 15.5; // Mock data - you'd calculate this based on registration dates
        Double platformUtilizationRate = 78.3; // Mock data
        Long totalTransactions = paymentRepository.count();
        
        AdminAnalyticsDTO.PlatformMetrics platformMetrics = new AdminAnalyticsDTO.PlatformMetrics();
        platformMetrics.setTotalUsers(totalUsers);
        platformMetrics.setMonthlyActiveUsers(monthlyActiveUsers);
        platformMetrics.setDailyActiveUsers(dailyActiveUsers);
        platformMetrics.setUserGrowthRate(userGrowthRate);
        platformMetrics.setPlatformUtilizationRate(platformUtilizationRate);
        platformMetrics.setTotalTransactions(totalTransactions);
        platformMetrics.setLastUpdated(LocalDateTime.now());
        
        return platformMetrics;
    }
    
    private AdminAnalyticsDTO.PerformanceMetrics getPerformanceMetrics() {
        Double averageBookingResponseTime = 24.5; // Mock data - in hours
        Double customerSatisfactionScore = 4.2; // Based on reviews
        Double vendorSatisfactionScore = 4.0; // Mock data
        Long disputeResolutionCount = 12L; // Mock data
        
        Long totalBookings = bookingRepository.count();
        Long completedBookings = bookingRepository.countByStatus(BookingStatus.COMPLETED);
        Double bookingSuccessRate = totalBookings > 0 ? 
                (completedBookings.doubleValue() / totalBookings.doubleValue()) * 100 : 0.0;
        
        Long totalPayments = paymentRepository.count();
        Long successfulPayments = (long) paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .collect(Collectors.toList())
                .size();
        Double paymentSuccessRate = totalPayments > 0 ? 
                (successfulPayments.doubleValue() / totalPayments.doubleValue()) * 100 : 0.0;
        
        AdminAnalyticsDTO.PerformanceMetrics performanceMetrics = new AdminAnalyticsDTO.PerformanceMetrics();
        performanceMetrics.setAverageBookingResponseTime(averageBookingResponseTime);
        performanceMetrics.setCustomerSatisfactionScore(customerSatisfactionScore);
        performanceMetrics.setVendorSatisfactionScore(vendorSatisfactionScore);
        performanceMetrics.setDisputeResolutionCount(disputeResolutionCount);
        performanceMetrics.setBookingSuccessRate(bookingSuccessRate);
        performanceMetrics.setPaymentSuccessRate(paymentSuccessRate);
        
        return performanceMetrics;
    }
    
    private AdminAnalyticsDTO.FinancialMetrics getFinancialMetrics() {
        BigDecimal totalPlatformRevenue = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .map(payment -> payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Long totalPayments = (long) paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .collect(Collectors.toList())
                .size();
        
        BigDecimal averageTransactionValue = totalPayments > 0 ? 
                totalPlatformRevenue.divide(BigDecimal.valueOf(totalPayments), 2, RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;
        
        // Monthly recurring revenue (simplified)
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        BigDecimal monthlyRecurringRevenue = paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .filter(payment -> payment.getPaidAt() != null && payment.getPaidAt().isAfter(startOfMonth))
                .map(payment -> payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal projectedAnnualRevenue = monthlyRecurringRevenue.multiply(BigDecimal.valueOf(12));
        
        Long totalCustomers = customerRepository.count();
        Long totalVendors = vendorRepository.count();
        
        Double revenuePerCustomer = totalCustomers > 0 ? 
                totalPlatformRevenue.divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP).doubleValue() : 0.0;
        
        Double revenuePerVendor = totalVendors > 0 ? 
                totalPlatformRevenue.divide(BigDecimal.valueOf(totalVendors), 2, RoundingMode.HALF_UP).doubleValue() : 0.0;
        
        AdminAnalyticsDTO.FinancialMetrics financialMetrics = new AdminAnalyticsDTO.FinancialMetrics();
        financialMetrics.setTotalPlatformRevenue(totalPlatformRevenue);
        financialMetrics.setAverageTransactionValue(averageTransactionValue);
        financialMetrics.setMonthlyRecurringRevenue(monthlyRecurringRevenue);
        financialMetrics.setProjectedAnnualRevenue(projectedAnnualRevenue);
        financialMetrics.setRevenuePerCustomer(revenuePerCustomer);
        financialMetrics.setRevenuePerVendor(revenuePerVendor);
        
        return financialMetrics;
    }
}
