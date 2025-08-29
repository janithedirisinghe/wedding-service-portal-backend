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

#### Customer Operations
- `POST /customer/{customerId}/vendor-type` - Add single vendor type preference
- `POST /customer/{customerId}/vendor-types` - Add multiple vendor type preferences
- `GET /customer/{customerId}` - Get all preferences for a customer
- `GET /customer/{customerId}/vendor-type-names` - Get only vendor type names
- `PUT /customer/{customerId}` - Update all preferences (replace existing)
- `DELETE /customer/{customerId}/vendor-type` - Remove specific preference
- `DELETE /customer/{customerId}/all` - Remove all preferences
- `GET /customer/{customerId}/has-vendor-type` - Check if customer has specific preference

#### Analytics Operations
- `GET /vendor-type/{vendorType}/customers` - Get customers preferring specific vendor type
- `GET /vendor-types/distinct` - Get all distinct vendor types
- `GET /statistics` - Get vendor type statistics

#### Data Migration Operations
- `POST /sync/customer/{customerId}` - Sync single customer from old system
- `POST /sync/all` - Sync all customers from old system

## Usage Examples

### Add Vendor Type Preference
```bash
POST /api/customer-preferred-vendor-types/customer/1/vendor-type?vendorType=photographer
```

### Add Multiple Preferences
```bash
POST /api/customer-preferred-vendor-types/customer/1/vendor-types
Content-Type: application/json

["photographer", "catering", "decoration"]
```

### Get Customer Preferences
```bash
GET /api/customer-preferred-vendor-types/customer/1
```

### Update All Preferences
```bash
PUT /api/customer-preferred-vendor-types/customer/1
Content-Type: application/json

["photographer", "music", "venue"]
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
