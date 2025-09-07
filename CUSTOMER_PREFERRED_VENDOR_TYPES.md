# Customer Preferred Vendor Types

## Overview
This module implements a new table-based approach for managing customer preferred vendor types. The new implementation creates a separate table `customer_preferred_vendor_types` to store the relationship between customers and their preferred vendor types.

## Database Schema

### Table: `customer_preferred_vendor_types`
| Column | Type | Description |
|--------|------|-------------|
| id | Long (Primary Key) | Auto-generated unique identifier |
| customer_id | Long (Foreign Key) | References customer.customer_id |
| vendor_type | String | The vendor type preference |

### Relationships
- **One Customer to Many CustomerPreferredVendorType**: A single customer can have multiple preferred vendor types
- **Many CustomerPreferredVendorType to One Customer**: Each preference record belongs to one customer

## Entity Classes

### CustomerPreferredVendorType
- Primary entity representing the customer-vendor type preference
- Contains fields: id, vendorType, customer
- Includes proper JPA annotations for database mapping
- Has constructors, getters, setters, equals, hashCode, and toString methods

### Customer (Modified)
- Added new relationship: `List<CustomerPreferredVendorType> customerPreferredVendorTypes`
- **Note**: The existing `preferredVendorTypes` field remains unchanged to maintain backward compatibility

## Repository

### CustomerPreferredVendorTypeRepository
Provides methods for:
- Finding preferences by customer
- Finding customers by vendor type
- Checking existence of specific preferences
- Deleting preferences
- Getting statistics and distinct vendor types

## Service Layer

### CustomerPreferredVendorTypeService
Provides business logic for:
- Adding single or multiple vendor type preferences
- Retrieving customer preferences
- Removing preferences
- Updating preferences
- Data migration from old system
- Statistics and analytics

## API Endpoints

### Base URL: `/api/customer-preferred-vendor-types`

#### User Authentication-Based Endpoints (Recommended)

These endpoints automatically get the customer from the authenticated user context:

##### My Preferences Operations
- `GET /my-preferences` - Get all my preferred vendor types
- `GET /my-preferences/names` - Get only my preferred vendor type names
- `POST /my-preferences/vendor-type?vendorType={type}` - Add a vendor type preference
- `POST /my-preferences/vendor-types` - Add multiple vendor type preferences (JSON body)
- `PUT /my-preferences` - Update all my preferences (replace existing with new list)
- `DELETE /my-preferences/vendor-type?vendorType={type}` - Remove specific preference
- `DELETE /my-preferences/all` - Remove all my preferences
- `GET /my-preferences/has-vendor-type?vendorType={type}` - Check if I have specific preference

#### Admin Operations
- `GET /admin/all` - Get all customer preferred vendor types
- `GET /admin/user/{userId}` - Get preferences for customer by user ID
- `GET /admin/customer/{customerId}` - Get preferences for customer by customer ID

#### Analytics Operations
- `GET /vendor-type/{vendorType}/customers` - Get customers preferring specific vendor type
- `GET /vendor-types/distinct` - Get all distinct vendor types
- `GET /statistics` - Get vendor type statistics

#### Data Migration Operations
- `POST /sync/customer/{customerId}` - Sync single customer from old system
- `POST /sync/all` - Sync all customers from old system

## Usage Examples

### Get My Preferences (Authenticated User)
```bash
GET /api/customer-preferred-vendor-types/my-preferences
Authorization: Bearer {jwt-token}
```

### Get My Preference Names Only
```bash
GET /api/customer-preferred-vendor-types/my-preferences/names
Authorization: Bearer {jwt-token}
```

### Add Single Vendor Type Preference
```bash
POST /api/customer-preferred-vendor-types/my-preferences/vendor-type?vendorType=photographer
Authorization: Bearer {jwt-token}
```

### Add Multiple Preferences
```bash
POST /api/customer-preferred-vendor-types/my-preferences/vendor-types
Authorization: Bearer {jwt-token}
Content-Type: application/json

["photographer", "catering", "decoration"]
```

### Update All Preferences
```bash
PUT /api/customer-preferred-vendor-types/my-preferences
Authorization: Bearer {jwt-token}
Content-Type: application/json

["photographer", "music", "venue"]
```

### Remove Specific Preference
```bash
DELETE /api/customer-preferred-vendor-types/my-preferences/vendor-type?vendorType=photographer
Authorization: Bearer {jwt-token}
```

### Remove All Preferences
```bash
DELETE /api/customer-preferred-vendor-types/my-preferences/all
Authorization: Bearer {jwt-token}
```

### Check If I Have Specific Preference
```bash
GET /api/customer-preferred-vendor-types/my-preferences/has-vendor-type?vendorType=photographer
Authorization: Bearer {jwt-token}
```

### Admin: Get Preferences by User ID
```bash
GET /api/customer-preferred-vendor-types/admin/user/123
Authorization: Bearer {admin-jwt-token}
```

### Admin: Get All Customer Preferences
```bash
GET /api/customer-preferred-vendor-types/admin/all
Authorization: Bearer {admin-jwt-token}
```

## Data Migration

The service includes methods to migrate data from the existing `preferredVendorTypes` ElementCollection to the new table structure:

1. `syncCustomerPreferredVendorTypes(customerId)` - Migrates single customer
2. `syncAllCustomerPreferredVendorTypes()` - Migrates all customers

This ensures backward compatibility and smooth transition from the old system.

## Benefits

1. **Better Performance**: Separate table allows for better indexing and querying
2. **Flexibility**: Easier to add metadata like creation date, priority, etc.
3. **Analytics**: Better support for analytics and reporting
4. **Scalability**: More efficient for large datasets
5. **Referential Integrity**: Proper foreign key constraints ensure data consistency

## Backward Compatibility

- The existing `preferredVendorTypes` field in Customer entity remains unchanged
- Existing APIs continue to work without modification
- Data migration tools provided for smooth transition
