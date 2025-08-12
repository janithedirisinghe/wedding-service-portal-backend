# Wedding Service Provider Chat API Documentation

## Overview
This chat system allows real-time communication between customers and vendors using WebSocket connections and REST APIs. The system includes:

- Real-time messaging with WebSocket
- Message history and pagination
- HTTP-only cookie authentication for security
- Message status tracking (sent, delivered, read)

## Authentication
This API uses HTTP-only cookies for authentication. The JWT token should be stored in a cookie named `auth_token`. The system also supports Authorization header as a fallback for API testing tools like Postman.

## REST API Endpoints

### 1. Start a Chat (Customer Only)
```http
POST /api/chat/start
Cookie: auth_token=<JWT_TOKEN>
Content-Type: application/json

{
    "vendorId": 123,
    "initialMessage": "Hi, I'm interested in your wedding photography services."
}
```

### 2. Send a Message
```http
POST /api/chat/message
Cookie: auth_token=<JWT_TOKEN>
Content-Type: application/json

{
    "chatRoomId": 456,
    "content": "What are your rates for wedding photography?",
    "messageType": "TEXT"
}
```

### 3. Get User's Chat Rooms
```http
GET /api/chat/rooms
Cookie: auth_token=<JWT_TOKEN>
```

### 4. Get Messages in a Chat Room
```http
GET /api/chat/room/456/messages?page=0&size=50
Cookie: auth_token=<JWT_TOKEN>
```

### 5. Mark Messages as Read
```http
PUT /api/chat/room/456/mark-read
Cookie: auth_token=<JWT_TOKEN>
```

## WebSocket Connection

### Connection URL
```
ws://localhost:8080/ws
```

### JavaScript Client Example
```javascript
// Include SockJS and STOMP libraries
// <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
// <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@6/bundles/stomp.umd.min.js"></script>

class ChatClient {
    constructor() {
        this.stompClient = null;
    }

    connect() {
        // No need to pass JWT token explicitly since it's in HTTP-only cookie
        const socket = new SockJS('/ws');
        this.stompClient = new StompJs.Client({
            webSocketFactory: () => socket,
            debug: function (str) {
                console.log(str);
            },
            onConnect: (frame) => {
                console.log('Connected: ' + frame);
                this.onConnected();
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            }
        });

        this.stompClient.activate();
    }

    onConnected() {
        console.log('WebSocket connected');
    }

    subscribeToRoom(chatRoomId) {
        return this.stompClient.subscribe(`/topic/chat/${chatRoomId}`, (message) => {
            const chatMessage = JSON.parse(message.body);
            this.onMessageReceived(chatMessage);
        });
    }

    sendMessage(chatRoomId, content, messageType = 'TEXT') {
        if (this.stompClient && this.stompClient.connected) {
            const message = {
                chatRoomId: chatRoomId,
                content: content,
                messageType: messageType
            };
            this.stompClient.publish({
                destination: `/app/chat/${chatRoomId}`,
                body: JSON.stringify(message)
            });
        }
    }

    onMessageReceived(message) {
        console.log('Received message:', message);
        // Handle the received message in your UI
        this.displayMessage(message);
    }

    displayMessage(message) {
        // Implement your message display logic here
        const messageElement = document.createElement('div');
        messageElement.innerHTML = `
            <div class="message">
                <strong>${message.senderName}:</strong>
                <span>${message.content}</span>
                <small>${new Date(message.sentAt).toLocaleString()}</small>
            </div>
        `;
        document.getElementById('messages').appendChild(messageElement);
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.deactivate();
        }
        console.log('WebSocket disconnected');
    }
}

// Usage
const chatClient = new ChatClient();
chatClient.connect(); // JWT token automatically sent via HTTP-only cookie

// Subscribe to a specific chat room
const subscription = chatClient.subscribeToRoom(456);

// Send a message
chatClient.sendMessage(456, 'Hello from WebSocket!');

// Unsubscribe when leaving the room
// subscription.unsubscribe();
```

### Angular Client Example
```typescript
import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private stompClient: Client;

  constructor() {}

  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.stompClient = new Client({
        // JWT token automatically sent via HTTP-only cookie
        webSocketFactory: () => new SockJS('/ws'),
        onConnect: () => {
          console.log('Connected to WebSocket');
          resolve();
        },
        onStompError: (error) => {
          console.error('STOMP error:', error);
          reject(error);
        }
      });

      this.stompClient.activate();
    });
  }

  subscribeToRoom(chatRoomId: number, callback: (message: any) => void) {
    return this.stompClient.subscribe(`/topic/chat/${chatRoomId}`, (message) => {
      callback(JSON.parse(message.body));
    });
  }

  sendMessage(chatRoomId: number, content: string, messageType: string = 'TEXT') {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: `/app/chat/${chatRoomId}`,
        body: JSON.stringify({
          chatRoomId,
          content,
          messageType
        })
      });
    }
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}
```

## Message Types
- `TEXT`: Regular text message
- `IMAGE`: Image attachment
- `FILE`: File attachment
- `SYSTEM`: System-generated message

## Message Status
- `SENT`: Message has been sent
- `DELIVERED`: Message has been delivered to recipient
- `READ`: Message has been read by recipient

## Error Handling
- Always include JWT token in requests
- Check connection status before sending messages
- Handle WebSocket connection failures gracefully
- Implement retry logic for failed connections

## Security Notes
- HTTP-only cookies are used for authentication to prevent XSS attacks
- Authorization header is supported as fallback for API testing tools
- Users can only access their own chat rooms
- Customer can only start chats with vendors
- Both customers and vendors can send messages once a chat is started
- WebSocket connections are authenticated via HTTP-only cookies during handshake

## Database Tables Created
1. `chat_rooms` - Stores chat room information
2. `chat_messages` - Stores individual messages

## Usage Flow
1. Customer uses REST API to start a chat with a vendor
2. Both parties connect to WebSocket using their JWT tokens
3. They subscribe to their specific chat room topic
4. Real-time messages are sent/received via WebSocket
5. Message history is retrieved via REST API
6. Messages are marked as read via REST API
