# Admin Dashboard Analytics API Documentation

This document provides comprehensive information about the analytical dashboard APIs for the Wedding Service Provider Management System admin dashboard.

## Overview

The admin dashboard provides meaningful analytical insights through various API endpoints that deliver real-time data about platform performance, revenue, bookings, vendors, and customers.

## API Endpoints

### 1. Dashboard Statistics API

**Endpoint:** `GET /admin/dashboard/stats`
**Authorization:** Admin role required
**Description:** Returns comprehensive dashboard statistics for the admin home page

**Response Structure:**
```json
{
  "overviewStats": {
    "totalCustomers": 1250,
    "totalVendors": 450,
    "totalBookings": 3890,
    "totalServices": 2340,
    "totalRevenue": 125000.00,
    "pendingVerifications": 23,
    "activeBookings": 156,
    "averageRating": 4.3
  },
  "revenueStats": {
    "monthlyRevenue": 15000.00,
    "yearlyRevenue": 125000.00,
    "previousMonthRevenue": 12000.00,
    "revenueGrowthRate": 25.0,
    "monthlyRevenueChart": [...],
    "revenueByServiceType": {...}
  },
  "bookingStats": {
    "pendingBookings": 45,
    "confirmedBookings": 123,
    "completedBookings": 890,
    "cancelledBookings": 67,
    "bookingCompletionRate": 85.5,
    "bookingsByStatus": {...},
    "dailyBookingsChart": [...]
  },
  "vendorStats": {
    "totalVendors": 450,
    "verifiedVendors": 398,
    "pendingVerifications": 52,
    "activeVendors": 423,
    "vendorsByType": {...},
    "averageVendorRating": 4.2,
    "topPerformingVendors": [...]
  },
  "customerStats": {
    "totalCustomers": 1250,
    "activeCustomers": 890,
    "newCustomersThisMonth": 78,
    "customerRetentionRate": 72.5,
    "customerGrowthChart": [...]
  },
  "recentActivities": [...]
}
```

### 2. Detailed Analytics API

**Endpoint:** `GET /admin/analytics/detailed`
**Authorization:** Admin role required
**Description:** Returns detailed analytics including platform, performance, and financial metrics

**Response Structure:**
```json
{
  "platformMetrics": {
    "totalUsers": 1700,
    "monthlyActiveUsers": 1200,
    "dailyActiveUsers": 400,
    "userGrowthRate": 15.5,
    "platformUtilizationRate": 78.3,
    "totalTransactions": 3890,
    "lastUpdated": "2025-02-16T18:30:00"
  },
  "performanceMetrics": {
    "averageBookingResponseTime": 24.5,
    "customerSatisfactionScore": 4.2,
    "vendorSatisfactionScore": 4.0,
    "disputeResolutionCount": 12,
    "bookingSuccessRate": 85.5,
    "paymentSuccessRate": 96.2
  },
  "financialMetrics": {
    "totalPlatformRevenue": 125000.00,
    "averageTransactionValue": 320.50,
    "monthlyRecurringRevenue": 15000.00,
    "projectedAnnualRevenue": 180000.00,
    "revenuePerCustomer": 100.00,
    "revenuePerVendor": 277.78
  }
}
```

### 3. Specific Analytics Endpoints

#### Revenue Analytics
- **Endpoint:** `GET /admin/analytics/revenue`
- **Description:** Returns revenue-specific analytics
- **Response:** Revenue stats portion of dashboard statistics

#### Booking Analytics
- **Endpoint:** `GET /admin/analytics/bookings`
- **Description:** Returns booking-specific analytics
- **Response:** Booking stats portion of dashboard statistics

#### Vendor Analytics
- **Endpoint:** `GET /admin/analytics/vendors`
- **Description:** Returns vendor-specific analytics
- **Response:** Vendor stats portion of dashboard statistics

#### Customer Analytics
- **Endpoint:** `GET /admin/analytics/customers`
- **Description:** Returns customer-specific analytics
- **Response:** Customer stats portion of dashboard statistics

#### Platform Overview
- **Endpoint:** `GET /admin/analytics/platform-overview`
- **Description:** Returns platform overview metrics
- **Response:** Overview stats portion of dashboard statistics

#### Recent Activities
- **Endpoint:** `GET /admin/analytics/recent-activities`
- **Description:** Returns recent platform activities
- **Response:** List of recent activities with type, description, timestamp, and user info

### 4. Key Performance Indicators (KPI) API

**Endpoint:** `GET /admin/analytics/kpi`
**Authorization:** Admin role required
**Description:** Returns key performance indicators for executive dashboard

**Response Structure:**
```json
{
  "totalRevenue": 125000.00,
  "monthlyRevenue": 15000.00,
  "revenueGrowthRate": 25.0,
  "averageTransactionValue": 320.50,
  "totalUsers": 1700,
  "totalBookings": 3890,
  "bookingCompletionRate": 85.5,
  "averageRating": 4.3,
  "newCustomersThisMonth": 78,
  "customerRetentionRate": 72.5,
  "vendorVerificationRate": 88.4,
  "paymentSuccessRate": 96.2,
  "customerSatisfactionScore": 4.2,
  "averageBookingResponseTime": 24.5
}
```

### 5. Dashboard Widgets API

**Endpoint:** `GET /admin/analytics/widgets`
**Authorization:** Admin role required
**Description:** Returns formatted data for dashboard widgets

**Response Structure:**
```json
{
  "overview": {
    "totalCustomers": 1250,
    "totalVendors": 450,
    "totalBookings": 3890,
    "totalRevenue": 125000.00
  },
  "revenue": {
    "monthlyRevenue": 15000.00,
    "growthRate": 25.0,
    "chartData": [...]
  },
  "bookingStatus": {
    "PENDING": 45,
    "CONFIRMED": 123,
    "COMPLETED": 890,
    "CANCELLED": 67
  },
  "topVendors": [...],
  "recentActivities": [...],
  "alerts": {
    "pendingVerifications": 23,
    "pendingBookings": 45
  }
}
```

### 6. Advanced Analytics Endpoints (Future Implementation)

#### Detailed Revenue Analytics
- **Endpoint:** `GET /admin/analytics/revenue/detailed`
- **Description:** Comprehensive revenue analytics with trends and breakdowns
- **Status:** Placeholder - requires service implementation

#### Detailed Booking Analytics
- **Endpoint:** `GET /admin/analytics/bookings/detailed`
- **Description:** Comprehensive booking analytics with performance metrics
- **Status:** Placeholder - requires service implementation

## Key Metrics Explained

### Revenue Metrics
- **Total Revenue:** Sum of all successful payments
- **Monthly Revenue:** Current month's revenue
- **Revenue Growth Rate:** Month-over-month growth percentage
- **Average Transaction Value:** Mean value of all transactions

### Booking Metrics
- **Booking Completion Rate:** Percentage of bookings marked as completed
- **Cancellation Rate:** Percentage of bookings cancelled
- **Average Response Time:** Mean time for vendors to respond to booking requests

### Customer Metrics
- **Active Customers:** Customers who made bookings in the last 3 months
- **Customer Retention Rate:** Percentage of customers with repeat bookings
- **New Customers:** First-time customers in the current month

### Vendor Metrics
- **Verification Rate:** Percentage of verified vendors
- **Average Vendor Rating:** Mean rating across all vendor reviews
- **Top Performing Vendors:** Vendors ranked by revenue and ratings

### Platform Metrics
- **Platform Utilization Rate:** Overall system usage percentage
- **User Growth Rate:** Rate of new user registrations
- **Payment Success Rate:** Percentage of successful payment transactions

## Data Refresh and Caching

- Dashboard statistics are calculated in real-time from the database
- For better performance, consider implementing caching for frequently accessed metrics
- Data is calculated based on current database state at the time of request

## Error Handling

All endpoints return appropriate HTTP status codes:
- **200 OK:** Successful response with data
- **401 Unauthorized:** Invalid or missing authentication
- **403 Forbidden:** Insufficient permissions (non-admin user)
- **500 Internal Server Error:** Server-side error during calculation

## Usage Examples

### Frontend Dashboard Implementation

```javascript
// Fetch dashboard stats for main dashboard
const dashboardStats = await fetch('/admin/dashboard/stats');

// Fetch KPIs for executive summary
const kpis = await fetch('/admin/analytics/kpi');

// Fetch widget data for dashboard cards
const widgets = await fetch('/admin/analytics/widgets');

// Fetch specific analytics for detailed views
const revenueAnalytics = await fetch('/admin/analytics/revenue');
const bookingAnalytics = await fetch('/admin/analytics/bookings');
```

### Dashboard Component Structure

```
Admin Dashboard
├── Overview Cards (from /analytics/widgets)
├── Revenue Chart (from /analytics/revenue)
├── Booking Status Chart (from /analytics/bookings)
├── Top Vendors Table (from /analytics/vendors)
├── Recent Activities Feed (from /analytics/recent-activities)
├── KPI Summary (from /analytics/kpi)
└── Alerts Panel (from /analytics/widgets alerts)
```

## Notes

1. All monetary values are returned as BigDecimal for precision
2. Dates are formatted in ISO 8601 format
3. Percentages are returned as doubles (e.g., 25.5 for 25.5%)
4. All endpoints require admin authentication
5. Some metrics use simplified calculations and can be enhanced based on requirements
6. Mock data is used in some calculations where historical data tracking isn't implemented

## Future Enhancements

1. Add date range filtering for analytics
2. Implement caching for better performance
3. Add export functionality for reports
4. Create scheduled report generation
5. Add comparative analytics (year-over-year, etc.)
6. Implement real-time updates using WebSocket
7. Add more granular vendor and customer segmentation
8. Create predictive analytics for revenue and growth forecasting
