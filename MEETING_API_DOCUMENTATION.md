# Meeting Management API Documentation

## Overview
The Meeting Management API provides endpoints for customers to request meetings with vendors and for vendors to manage those meeting requests. The system supports different meeting modes (in-person, virtual, phone call) and tracks the complete lifecycle of meetings from request to completion.

## Base URL
```
/api/meetings
```

## Authentication
All endpoints require a valid user ID in the path parameter. The system will automatically determine if the user is a customer or vendor and provide appropriate access.

## Entities

### Meeting Entity
- **meetingId**: Long - Unique identifier for the meeting
- **meetingDateTime**: LocalDateTime - Date and time of the meeting
- **meetingMood**: Enum - Type of meeting (IN_PERSON, VIRTUAL, PHONE_CALL)
- **location**: String - Meeting location or virtual meeting details
- **status**: Enum - Current status (PENDING, CONFIRMED, REJECTED, COMPLETED, CANCELLED)
- **notes**: String - Additional notes from customer
- **rejectionReason**: String - Reason for rejection (if applicable)
- **requestedAt**: LocalDateTime - When the meeting was requested
- **confirmedAt**: LocalDateTime - When the meeting was confirmed
- **customer**: Customer - Associated customer
- **vendor**: Vendor - Associated vendor

### Meeting Status Enum
- `PENDING` - Meeting request is awaiting vendor response
- `CONFIRMED` - Vendor has confirmed the meeting
- `REJECTED` - Vendor has rejected the meeting
- `COMPLETED` - Meeting has been completed
- `CANCELLED` - Meeting has been cancelled by either party

### Meeting Mood Enum
- `IN_PERSON` - Face-to-face meeting
- `VIRTUAL` - Online video meeting
- `PHONE_CALL` - Phone call meeting

## API Endpoints

### 1. Create Meeting Request (Customer)
**POST** `/api/meetings/request/{userId}`

Creates a new meeting request from a customer to a vendor.

**Path Parameters:**
- `userId` (Long) - Customer's user ID

**Request Body:**
```json
{
    "meetingDateTime": "2025-08-15 14:30:00",
    "meetingMood": "IN_PERSON",
    "location": "123 Main St, City, State",
    "vendorId": 5,
    "notes": "Looking forward to discussing wedding photography services"
}
```

**Response:**
```json
{
    "meetingId": 1,
    "meetingDateTime": "2025-08-15 14:30:00",
    "meetingMood": "IN_PERSON",
    "location": "123 Main St, City, State",
    "status": "PENDING",
    "notes": "Looking forward to discussing wedding photography services",
    "requestedAt": "2025-08-12 10:30:00",
    "customerId": 3,
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "vendorId": 5,
    "vendorBusinessName": "Perfect Moments Photography",
    "vendorEmail": "vendor@example.com"
}
```

### 2. Respond to Meeting Request (Vendor)
**PUT** `/api/meetings/respond/{userId}`

Allows vendors to respond to meeting requests (confirm or reject).

**Path Parameters:**
- `userId` (Long) - Vendor's user ID

**Request Body:**
```json
{
    "meetingId": 1,
    "status": "CONFIRMED"
}
```

Or for rejection:
```json
{
    "meetingId": 1,
    "status": "REJECTED",
    "rejectionReason": "Not available at that time"
}
```

**Response:**
```json
{
    "meetingId": 1,
    "meetingDateTime": "2025-08-15 14:30:00",
    "meetingMood": "IN_PERSON",
    "location": "123 Main St, City, State",
    "status": "CONFIRMED",
    "confirmedAt": "2025-08-12 11:00:00",
    "customerId": 3,
    "customerName": "John Doe",
    "vendorId": 5,
    "vendorBusinessName": "Perfect Moments Photography"
}
```

### 3. Get User Meetings
**GET** `/api/meetings/user/{userId}`

Retrieves all meetings for a user (automatically detects if customer or vendor).

**Path Parameters:**
- `userId` (Long) - User ID

**Response:**
```json
[
    {
        "meetingId": 1,
        "meetingDateTime": "2025-08-15 14:30:00",
        "meetingMood": "IN_PERSON",
        "location": "123 Main St, City, State",
        "status": "CONFIRMED",
        "notes": "Looking forward to discussing wedding photography services",
        "requestedAt": "2025-08-12 10:30:00",
        "confirmedAt": "2025-08-12 11:00:00",
        "customerId": 3,
        "customerName": "John Doe",
        "vendorId": 5,
        "vendorBusinessName": "Perfect Moments Photography"
    }
]
```

### 4. Get Customer Meetings
**GET** `/api/meetings/customer/{userId}`

Retrieves all meetings for a specific customer.

### 5. Get Vendor Meetings
**GET** `/api/meetings/vendor/{userId}`

Retrieves all meetings for a specific vendor.

### 6. Get Meetings by Status (Customer)
**GET** `/api/meetings/customer/{userId}/status/{status}`

Retrieves customer meetings filtered by status.

**Path Parameters:**
- `userId` (Long) - Customer's user ID
- `status` (String) - Meeting status (PENDING, CONFIRMED, REJECTED, COMPLETED, CANCELLED)

### 7. Get Meetings by Status (Vendor)
**GET** `/api/meetings/vendor/{userId}/status/{status}`

Retrieves vendor meetings filtered by status.

### 8. Get Upcoming Meetings (Customer)
**GET** `/api/meetings/customer/{userId}/upcoming`

Retrieves upcoming confirmed meetings for a customer.

### 9. Get Upcoming Meetings (Vendor)
**GET** `/api/meetings/vendor/{userId}/upcoming`

Retrieves upcoming confirmed meetings for a vendor.

### 10. Get Meeting by ID
**GET** `/api/meetings/{meetingId}/user/{userId}`

Retrieves a specific meeting by ID with proper authorization.

**Path Parameters:**
- `meetingId` (Long) - Meeting ID
- `userId` (Long) - User ID (must be either the customer or vendor of the meeting)

### 11. Cancel Meeting
**PUT** `/api/meetings/{meetingId}/cancel/{userId}`

Cancels a meeting (can be done by either customer or vendor).

**Path Parameters:**
- `meetingId` (Long) - Meeting ID
- `userId` (Long) - User ID

### 12. Complete Meeting (Vendor Only)
**PUT** `/api/meetings/{meetingId}/complete/{userId}`

Marks a meeting as completed (typically done by vendor after the meeting).

**Path Parameters:**
- `meetingId` (Long) - Meeting ID
- `userId` (Long) - Vendor's user ID

### 13. Get Meeting Statuses
**GET** `/api/meetings/statuses`

Returns all possible meeting statuses.

**Response:**
```json
["PENDING", "CONFIRMED", "REJECTED", "COMPLETED", "CANCELLED"]
```

### 14. Get Meeting Moods
**GET** `/api/meetings/moods`

Returns all possible meeting moods.

**Response:**
```json
["IN_PERSON", "VIRTUAL", "PHONE_CALL"]
```

## Error Responses

All endpoints return appropriate HTTP status codes and error messages:

### 400 Bad Request
```json
{
    "error": "User not found with ID: 123"
}
```

### 500 Internal Server Error
```json
{
    "error": "Failed to create meeting request: Database connection error"
}
```

## Usage Examples

### Customer requesting a meeting:
1. Customer calls `POST /api/meetings/request/{customerId}` with meeting details
2. System creates meeting with status "PENDING"
3. Vendor receives notification (can be implemented separately)

### Vendor responding to meeting:
1. Vendor calls `GET /api/meetings/vendor/{vendorId}/status/PENDING` to see pending requests
2. Vendor calls `PUT /api/meetings/respond/{vendorId}` to confirm or reject

### Viewing upcoming meetings:
1. Customer calls `GET /api/meetings/customer/{customerId}/upcoming`
2. Vendor calls `GET /api/meetings/vendor/{vendorId}/upcoming`

## Business Logic Notes

1. **Authorization**: Users can only view/modify meetings they are associated with (either as customer or vendor)
2. **Status Transitions**: 
   - PENDING → CONFIRMED/REJECTED (by vendor)
   - CONFIRMED → COMPLETED (by vendor) or CANCELLED (by either)
   - Any status → CANCELLED (except COMPLETED)
3. **Date Validation**: Meeting dates must be in the future
4. **User Identification**: The system automatically determines if a user is a customer or vendor based on the user ID
