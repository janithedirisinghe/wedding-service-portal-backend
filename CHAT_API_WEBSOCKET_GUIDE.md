# Chat API and WebSocket Integration Guide

## Overview
This guide provides comprehensive documentation for integrating the chat functionality into the frontend application. The backend supports both REST API calls for chat management and WebSocket connections for real-time messaging.

## Authentication
All chat endpoints require JWT authentication. The token should be sent via:
- **HTTP-only cookie**: `auth_token`
- **Fallback**: `Authorization: Bearer <token>` header

## REST API Endpoints

### 1. Start a New Chat
**Endpoint:** `POST /api/chat/start`
**Description:** Creates a new chat room between a customer and vendor

**Request Body:**
```json
{
  "vendorId": 123,
  "initialMessage": "Hello, I'm interested in your services"
}
```

**Response:**
```json
{
  "chatRoomId": 456,
  "roomName": "customer_789_vendor_123",
  "customerId": 789,
  "vendorId": 123,
  "status": "ACTIVE",
  "createdAt": "2025-08-31T10:30:00",
  "lastMessageAt": "2025-08-31T10:30:00"
}
```

**Error Responses:**
- `400 Bad Request`: Invalid vendor ID or user not a customer
- `404 Not Found`: Vendor not found

### 2. Get All Vendors
**Endpoint:** `GET /api/chat/vendors`
**Description:** Get all vendors for chat functionality

**Response:**
```json
[
  {
    "venderId": 123,
    "businessName": "Elegant Weddings Co.",
    "profileImageUrl": "https://example.com/profile.jpg",
    "venType": "Wedding Planner",
    "isActive": true,
    "verify": true
  }
]
```

### 3. Send a Message
**Endpoint:** `POST /api/chat/message`
**Description:** Sends a message in an existing chat room

**Request Body:**
```json
{
  "chatRoomId": 456,
  "content": "This is my message",
  "messageType": "TEXT",
  "attachmentUrl": null,
  "attachmentType": null
}
```

**Response:**
```json
{
  "messageId": 789,
  "chatRoomId": 456,
  "senderId": 123,
  "senderName": "John Doe",
  "senderType": "CUSTOMER",
  "content": "This is my message",
  "sentAt": "2025-08-31T10:35:00",
  "messageType": "TEXT",
  "status": "SENT",
  "readAt": null,
  "attachmentUrl": null,
  "attachmentType": null
}
```

### 3. Get Customer's Chat Rooms
**Endpoint:** `GET /api/chat/customer/rooms?userId={userId}`
**Description:** Get all chat rooms for a specific customer

**Parameters:**
- `userId` (required) - The user ID of the customer

**Response:**
```json
[
  {
    "chatRoomId": 456,
    "roomName": "customer_789_vendor_123",
    "customerId": 789,
    "customerName": "John Doe",
    "customerProfileImageUrl": "https://example.com/customer-profile.jpg",
    "vendorId": 123,
    "vendorName": "Jane Smith",
    "vendorBusinessName": "Elegant Weddings Co.",
    "vendorProfileImageUrl": "https://example.com/vendor-profile.jpg",
    "status": "ACTIVE",
    "createdAt": "2025-08-31T10:30:00",
    "lastMessageAt": "2025-08-31T10:35:00",
    "unreadCount": 2,
    "recentMessages": [...]
  }
]
```

### 4. Get Vendor's Chat Rooms
**Endpoint:** `GET /api/chat/vendor/rooms?userId={userId}`
**Description:** Get all chat rooms for a specific vendor

**Parameters:**
- `userId` (required) - The user ID of the vendor

**Response:**
```json
[
  {
    "chatRoomId": 456,
    "roomName": "customer_789_vendor_123",
    "customerId": 789,
    "customerName": "John Doe",
    "customerProfileImageUrl": "https://example.com/customer-profile.jpg",
    "vendorId": 123,
    "vendorName": "Jane Smith",
    "vendorBusinessName": "Elegant Weddings Co.",
    "vendorProfileImageUrl": "https://example.com/vendor-profile.jpg",
    "status": "ACTIVE",
    "createdAt": "2025-08-31T10:30:00",
    "lastMessageAt": "2025-08-31T10:35:00",
    "unreadCount": 2,
    "recentMessages": [...]
  }
]
```

### 4. Get Chat Messages
**Endpoint:** `GET /api/chat/room/{chatRoomId}/messages?page=0&size=50`
**Description:** Retrieves paginated messages from a chat room

**Parameters:**
- `chatRoomId`: ID of the chat room
- `page`: Page number (default: 0)
- `size`: Number of messages per page (default: 50)

**Response:**
```json
[
  {
    "messageId": 789,
    "chatRoomId": 456,
    "senderId": 123,
    "senderName": "John Doe",
    "senderType": "CUSTOMER",
    "content": "Hello!",
    "sentAt": "2025-08-31T10:30:00",
    "messageType": "TEXT",
    "status": "READ",
    "readAt": "2025-08-31T10:31:00",
    "attachmentUrl": null,
    "attachmentType": null
  }
]
```

### 5. Mark Messages as Read
**Endpoint:** `PUT /api/chat/room/{chatRoomId}/mark-read`
**Description:** Marks all messages in a chat room as read for the current user

**Response:** `200 OK`

## WebSocket Configuration

### Connection Setup
**WebSocket URL:** `ws://your-server/ws`
**STOMP Endpoint:** `/ws` (with SockJS fallback)

### Frontend Setup (JavaScript/React)

#### 1. Install Dependencies
```bash
npm install @stomp/stompjs sockjs-client
```

#### 2. WebSocket Connection Setup
```javascript
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// Create STOMP client
const stompClient = new Client({
  webSocketFactory: () => new SockJS('http://your-server/ws'),
  connectHeaders: {
    // JWT token will be sent via cookie automatically
  },
  debug: (str) => {
    console.log(str);
  },
  reconnectDelay: 5000,
  heartbeatIncoming: 4000,
  heartbeatOutgoing: 4000,
});

// Connection callbacks
stompClient.onConnect = (frame) => {
  console.log('Connected to WebSocket');
  // Subscribe to chat rooms
};

stompClient.onStompError = (frame) => {
  console.error('STOMP error:', frame.headers['message']);
  console.error('Details:', frame.body);
};

// Activate the client
stompClient.activate();
```

#### 3. Subscribe to Chat Rooms
```javascript
// Subscribe to a specific chat room
const subscribeToChatRoom = (chatRoomId) => {
  const subscription = stompClient.subscribe(
    `/topic/chat/${chatRoomId}`,
    (message) => {
      const messageData = JSON.parse(message.body);
      console.log('Received message:', messageData);
      // Handle incoming message
      handleIncomingMessage(messageData);
    }
  );
  return subscription;
};
```

#### 4. Send Messages via WebSocket
```javascript
const sendMessage = (chatRoomId, messageContent) => {
  if (stompClient.connected) {
    stompClient.publish({
      destination: `/app/chat/${chatRoomId}`,
      body: JSON.stringify({
        chatRoomId: chatRoomId,
        content: messageContent,
        messageType: 'TEXT'
      })
    });
  }
};
```

#### 5. Join/Leave Chat Room Notifications
```javascript
// Send join notification
const joinChatRoom = (chatRoomId) => {
  stompClient.publish({
    destination: `/app/chat/${chatRoomId}/join`,
    body: JSON.stringify({})
  });
};

// Send leave notification
const leaveChatRoom = (chatRoomId) => {
  stompClient.publish({
    destination: `/app/chat/${chatRoomId}/leave`,
    body: JSON.stringify({})
  });
};
```

### React Hook Example
```javascript
import { useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const useWebSocketChat = (chatRoomId) => {
  const [messages, setMessages] = useState([]);
  const [isConnected, setIsConnected] = useState(false);
  const stompClientRef = useRef(null);

  useEffect(() => {
    // Initialize STOMP client
    const stompClient = new Client({
      webSocketFactory: () => new SockJS('http://your-server/ws'),
      reconnectDelay: 5000,
    });

    stompClient.onConnect = () => {
      setIsConnected(true);
      console.log('Connected to chat WebSocket');

      // Subscribe to chat room
      stompClient.subscribe(`/topic/chat/${chatRoomId}`, (message) => {
        const messageData = JSON.parse(message.body);
        setMessages(prev => [...prev, messageData]);
      });
    };

    stompClient.onDisconnect = () => {
      setIsConnected(false);
    };

    stompClient.onStompError = (frame) => {
      console.error('STOMP error:', frame.headers['message']);
    };

    stompClient.activate();
    stompClientRef.current = stompClient;

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.deactivate();
      }
    };
  }, [chatRoomId]);

  const sendMessage = (content) => {
    if (stompClientRef.current && stompClientRef.current.connected) {
      stompClientRef.current.publish({
        destination: `/app/chat/${chatRoomId}`,
        body: JSON.stringify({
          chatRoomId: chatRoomId,
          content: content,
          messageType: 'TEXT'
        })
      });
    }
  };

  return { messages, isConnected, sendMessage };
};
```

## Message Types and Status

### Message Types
- `TEXT`: Regular text message
- `IMAGE`: Image attachment
- `FILE`: File attachment
- `SYSTEM`: System-generated message

### Message Status
- `SENT`: Message sent successfully
- `DELIVERED`: Message delivered to recipient
- `READ`: Message read by recipient

### Sender Types
- `CUSTOMER`: Message from customer
- `VENDOR`: Message from vendor
- `SYSTEM`: System-generated message

## Error Handling

### WebSocket Connection Errors
```javascript
stompClient.onStompError = (frame) => {
  console.error('WebSocket connection failed:', frame.headers['message']);
  // Implement reconnection logic or show error to user
};
```

### Message Sending Errors
```javascript
// Handle errors in WebSocket message responses
const handleIncomingMessage = (messageData) => {
  if (messageData.content && messageData.content.startsWith('Error sending message:')) {
    console.error('Message send failed:', messageData.content);
    // Show error to user
  } else {
    // Process normal message
    updateChatMessages(messageData);
  }
};
```

### Authentication Errors
- WebSocket connections will fail if JWT token is invalid or missing
- REST API calls will return `401 Unauthorized` for invalid tokens
- Implement token refresh logic in your authentication system

## Best Practices

### 1. Connection Management
- Always check `stompClient.connected` before sending messages
- Implement automatic reconnection on disconnection
- Clean up subscriptions when components unmount

### 2. Message Handling
- Use message IDs to prevent duplicate message display
- Implement message status tracking (sent, delivered, read)
- Handle different message types appropriately

### 3. Performance
- Limit message history loading (use pagination)
- Implement message caching for better UX
- Use WebSocket for real-time updates, REST for initial data loading

### 4. Security
- Never send sensitive data through WebSocket messages
- Validate all message data on both frontend and backend
- Implement rate limiting for message sending

## Testing

### WebSocket Connection Test
```javascript
// Test connection
stompClient.activate();

// Test message sending
sendMessage(chatRoomId, 'Test message');

// Test subscription
subscribeToChatRoom(chatRoomId);
```

### API Endpoint Testing
Use tools like Postman or curl to test REST endpoints:
```bash
# Get chat rooms
curl -X GET "http://your-server/api/chat/rooms" \
  -H "Cookie: auth_token=your-jwt-token"

# Send message
curl -X POST "http://your-server/api/chat/message" \
  -H "Content-Type: application/json" \
  -H "Cookie: auth_token=your-jwt-token" \
  -d '{"chatRoomId": 456, "content": "Test message"}'
```

## Troubleshooting

### Common Issues

1. **WebSocket Connection Fails**
   - Check if JWT token is valid and present in cookies
   - Verify WebSocket URL is correct
   - Check browser console for CORS errors

2. **Messages Not Received**
   - Ensure subscription is active
   - Check if user has access to the chat room
   - Verify message format matches expected structure

3. **Authentication Errors**
   - Refresh JWT token if expired
   - Check cookie settings (HTTP-only, secure flags)
   - Verify token format and claims

4. **CORS Issues**
   - Backend allows origins with `setAllowedOriginPatterns("*")`
   - Check if frontend and backend are on same domain/port

### Debug Mode
Enable debug logging in development:
```javascript
const stompClient = new Client({
  // ... other config
  debug: (str) => {
    console.log('STOMP:', str);
  }
});
```

## Support
If you encounter issues not covered in this guide, please check:
1. Backend logs for error messages
2. Browser developer tools network tab
3. WebSocket connection status
4. JWT token validity

For additional help, contact the backend development team with specific error messages and reproduction steps.</content>
<parameter name="filePath">e:\Wedding Service Provider Backend\WeddingVenderMngSystem\CHAT_API_WEBSOCKET_GUIDE.md
