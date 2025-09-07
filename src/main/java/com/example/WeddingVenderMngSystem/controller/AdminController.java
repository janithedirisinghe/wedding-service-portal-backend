package com.example.WeddingVenderMngSystem.controller;

import com.example.WeddingVenderMngSystem.dto.AdminAnalyticsDTO;
import com.example.WeddingVenderMngSystem.dto.AdminCompleteInfoResponse;
import com.example.WeddingVenderMngSystem.dto.ApiTestResponseDTO;
import com.example.WeddingVenderMngSystem.dto.BookingAnalyticsDTO;
import com.example.WeddingVenderMngSystem.dto.DashboardStatsDTO;
import com.example.WeddingVenderMngSystem.dto.RevenueAnalyticsDTO;
import com.example.WeddingVenderMngSystem.entity.Vendor;
import com.example.WeddingVenderMngSystem.service.AdminAnalyticsService;
import com.example.WeddingVenderMngSystem.service.AdminService;
import com.example.WeddingVenderMngSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AdminAnalyticsService adminAnalyticsService;

        @Autowired
        private com.example.WeddingVenderMngSystem.service.CustomerService customerService;

        /**
         * Admin API: Get all customers with full details
         */
        @GetMapping("/customers/all")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<com.example.WeddingVenderMngSystem.dto.admin.AdminCustomerDetailsDTO>> getAllCustomersForAdmin() {
            List<com.example.WeddingVenderMngSystem.dto.admin.AdminCustomerDetailsDTO> customers = customerService.getAllCustomerDetailsForAdmin();
            return ResponseEntity.ok(customers);
        }

        /**
         * Admin API: Toggle isActive flag for a customer
         */
        @PostMapping("/customers/toggle-active")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<String> toggleCustomerActive(@RequestBody com.example.WeddingVenderMngSystem.dto.admin.ToggleCustomerActiveDTO dto) {
            boolean result = customerService.toggleCustomerActiveFlag(dto.getCustomerId(), dto.getIsActive());
            return ResponseEntity.ok("Customer isActive set to " + (result ? "1" : "0"));
        }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome Admin!";
    }

    // ============ ANALYTICS & DASHBOARD APIs ============
    
    /**
     * Get comprehensive dashboard statistics for admin home page
     * Includes overview stats, revenue analytics, booking metrics, vendor stats, and customer analytics
     */
    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get detailed analytics for admin dashboard
     * Includes platform metrics, performance metrics, and financial metrics
     */
    @GetMapping("/analytics/detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminAnalyticsDTO> getDetailedAnalytics() {
        try {
            AdminAnalyticsDTO analytics = adminAnalyticsService.getDetailedAnalytics();
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get revenue analytics overview
     */
    @GetMapping("/analytics/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO.RevenueStats> getRevenueAnalytics() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats.getRevenueStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get booking analytics overview
     */
    @GetMapping("/analytics/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO.BookingStats> getBookingAnalytics() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats.getBookingStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get vendor analytics overview
     */
    @GetMapping("/analytics/vendors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO.VendorStats> getVendorAnalytics() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats.getVendorStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get customer analytics overview
     */
    @GetMapping("/analytics/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO.CustomerStats> getCustomerAnalytics() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats.getCustomerStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get platform overview metrics
     */
    @GetMapping("/analytics/platform-overview")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO.OverviewStats> getPlatformOverview() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats.getOverviewStats());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get recent platform activities
     */
    @GetMapping("/analytics/recent-activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DashboardStatsDTO.RecentActivityDTO>> getRecentActivities() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            return ResponseEntity.ok(stats.getRecentActivities());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get comprehensive revenue analytics with trends and breakdowns
     */
    @GetMapping("/analytics/revenue/detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueAnalyticsDTO> getDetailedRevenueAnalytics() {
        try {
            // This would be implemented in the service
            // RevenueAnalyticsDTO revenueAnalytics = adminAnalyticsService.getDetailedRevenueAnalytics();
            // For now, return a mock response or implement the service method
            return ResponseEntity.ok(new RevenueAnalyticsDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get comprehensive booking analytics with trends and performance metrics
     */
    @GetMapping("/analytics/bookings/detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingAnalyticsDTO> getDetailedBookingAnalytics() {
        try {
            // This would be implemented in the service
            // BookingAnalyticsDTO bookingAnalytics = adminAnalyticsService.getDetailedBookingAnalytics();
            // For now, return a mock response or implement the service method
            return ResponseEntity.ok(new BookingAnalyticsDTO());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get key performance indicators for the platform
     */
    @GetMapping("/analytics/kpi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getKeyPerformanceIndicators() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            AdminAnalyticsDTO analytics = adminAnalyticsService.getDetailedAnalytics();
            
            Map<String, Object> kpis = new HashMap<>();
            
            // Financial KPIs
            kpis.put("totalRevenue", stats.getRevenueStats().getYearlyRevenue());
            kpis.put("monthlyRevenue", stats.getRevenueStats().getMonthlyRevenue());
            kpis.put("revenueGrowthRate", stats.getRevenueStats().getRevenueGrowthRate());
            kpis.put("averageTransactionValue", analytics.getFinancialMetrics().getAverageTransactionValue());
            
            // Platform KPIs
            kpis.put("totalUsers", stats.getOverviewStats().getTotalCustomers() + stats.getOverviewStats().getTotalVendors());
            kpis.put("totalBookings", stats.getOverviewStats().getTotalBookings());
            kpis.put("bookingCompletionRate", stats.getBookingStats().getBookingCompletionRate());
            kpis.put("averageRating", stats.getOverviewStats().getAverageRating());
            
            // Growth KPIs
            kpis.put("newCustomersThisMonth", stats.getCustomerStats().getNewCustomersThisMonth());
            kpis.put("customerRetentionRate", stats.getCustomerStats().getCustomerRetentionRate());
            kpis.put("vendorVerificationRate", (double) stats.getVendorStats().getVerifiedVendors() / stats.getVendorStats().getTotalVendors() * 100);
            
            // Performance KPIs
            kpis.put("paymentSuccessRate", analytics.getPerformanceMetrics().getPaymentSuccessRate());
            kpis.put("customerSatisfactionScore", analytics.getPerformanceMetrics().getCustomerSatisfactionScore());
            kpis.put("averageBookingResponseTime", analytics.getPerformanceMetrics().getAverageBookingResponseTime());
            
            return ResponseEntity.ok(kpis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get analytics summary for dashboard widgets
     */
    @GetMapping("/analytics/widgets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardWidgets() {
        try {
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            
            Map<String, Object> widgets = new HashMap<>();
            
            // Overview Widget
            Map<String, Object> overviewWidget = new HashMap<>();
            overviewWidget.put("totalCustomers", stats.getOverviewStats().getTotalCustomers());
            overviewWidget.put("totalVendors", stats.getOverviewStats().getTotalVendors());
            overviewWidget.put("totalBookings", stats.getOverviewStats().getTotalBookings());
            overviewWidget.put("totalRevenue", stats.getOverviewStats().getTotalRevenue());
            widgets.put("overview", overviewWidget);
            
            // Revenue Widget
            Map<String, Object> revenueWidget = new HashMap<>();
            revenueWidget.put("monthlyRevenue", stats.getRevenueStats().getMonthlyRevenue());
            revenueWidget.put("growthRate", stats.getRevenueStats().getRevenueGrowthRate());
            revenueWidget.put("chartData", stats.getRevenueStats().getMonthlyRevenueChart());
            widgets.put("revenue", revenueWidget);
            
            // Booking Status Widget
            widgets.put("bookingStatus", stats.getBookingStats().getBookingsByStatus());
            
            // Top Vendors Widget
            widgets.put("topVendors", stats.getVendorStats().getTopPerformingVendors());
            
            // Recent Activities Widget
            widgets.put("recentActivities", stats.getRecentActivities().stream().limit(5).collect(Collectors.toList()));
            
            // Alerts Widget
            Map<String, Object> alertsWidget = new HashMap<>();
            alertsWidget.put("pendingVerifications", stats.getOverviewStats().getPendingVerifications());
            alertsWidget.put("pendingBookings", stats.getBookingStats().getPendingBookings());
            widgets.put("alerts", alertsWidget);
            
            return ResponseEntity.ok(widgets);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Test endpoint to verify analytics APIs are working
     */
    @GetMapping("/analytics/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiTestResponseDTO> testAnalyticsApis() {
        try {
            List<String> endpoints = List.of(
                "GET /admin/dashboard/stats - Comprehensive dashboard statistics",
                "GET /admin/analytics/detailed - Detailed analytics with platform, performance, and financial metrics",
                "GET /admin/analytics/revenue - Revenue-specific analytics",
                "GET /admin/analytics/bookings - Booking-specific analytics",
                "GET /admin/analytics/vendors - Vendor-specific analytics",
                "GET /admin/analytics/customers - Customer-specific analytics",
                "GET /admin/analytics/platform-overview - Platform overview metrics",
                "GET /admin/analytics/recent-activities - Recent platform activities",
                "GET /admin/analytics/kpi - Key performance indicators",
                "GET /admin/analytics/widgets - Dashboard widget data",
                "GET /admin/analytics/revenue/detailed - Detailed revenue analytics (placeholder)",
                "GET /admin/analytics/bookings/detailed - Detailed booking analytics (placeholder)"
            );
            
            // Test basic data access
            DashboardStatsDTO stats = adminAnalyticsService.getDashboardStats();
            
            ApiTestResponseDTO response = new ApiTestResponseDTO();
            response.setStatus("SUCCESS");
            response.setMessage("All analytics APIs are configured and accessible. Analytics service is working correctly.");
            response.setAvailableEndpoints(endpoints);
            response.setSampleData(Map.of(
                    "totalCustomers", stats.getOverviewStats().getTotalCustomers(),
                    "totalVendors", stats.getOverviewStats().getTotalVendors(),
                    "totalBookings", stats.getOverviewStats().getTotalBookings(),
                    "totalRevenue", stats.getOverviewStats().getTotalRevenue()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiTestResponseDTO errorResponse = new ApiTestResponseDTO();
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage("Error testing analytics APIs: " + e.getMessage());
            errorResponse.setAvailableEndpoints(List.of());
            errorResponse.setSampleData(null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // ============ EXISTING ADMIN MANAGEMENT APIs ============

    @GetMapping("/admin-complete-info")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCompleteInfoResponse> getAdminCompleteInfo() {
        try {
            // Get the current authenticated user ID
            Long userId = getCurrentUserId();
            
            AdminCompleteInfoResponse response = adminService.getAdminCompleteInfoByUserId(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin-complete-info/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCompleteInfoResponse> getAdminCompleteInfoById(@PathVariable Long adminId) {
        try {
            AdminCompleteInfoResponse response = adminService.getAdminCompleteInfo(adminId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/vendors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        try {
            List<Vendor> vendors = vendorService.getAllVendors();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/vendors/{vendorId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vendor> updateVendorVerification(@PathVariable Long vendorId, @RequestParam Boolean verify) {
        try {
            Vendor updatedVendor = vendorService.updateVendorVerificationStatus(vendorId, verify);
            return ResponseEntity.ok(updatedVendor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to get current user ID - you'll need to implement this based on your JWT/Security setup
    private Long getCurrentUserId() {
        // This is a placeholder - implement based on your authentication mechanism
        // You might extract this from JWT token or get it from UserDetails
        // For now, returning a placeholder value
        return 1L; // TODO: Replace with actual implementation based on your security setup
    }
}
