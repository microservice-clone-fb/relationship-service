# Friend Request API Guide

## Overview
Hướng dẫn sử dụng các API liên quan đến lời mời kết bạn trong Relationship Service.

## Endpoints

### 1. Gửi lời mời kết bạn
**POST** `/users/friend-requests/send`

Request body:
```json
{
  "requesterId": "user123",
  "targetUserId": "user456",
  "message": "Hi, let's be friends"
}
```

Response:
```json
{
  "result": "Friend request sent"
}
```

---

### 2. Hủy lời mời kết bạn
**POST** `/users/friend-requests/cancel`

Request body:
```json
{
  "requesterId": "user123",
  "targetUserId": "user456"
}
```

Response:
```json
{
  "result": "Friend request cancelled"
}
```

---

### 3. Trả lời lời mời kết bạn (Chấp nhận/Từ chối)
**POST** `/users/friend-requests/respond`

Request body:
```json
{
  "requesterId": "user456",
  "targetUserId": "user123",
  "decision": "ACCEPT"  // or "REJECT"
}
```

Response:
```json
{
  "result": "Friend request updated"
}
```

---

### 4. **[NEW]** Lấy danh sách những người đã gửi lời mời kết bạn cho mình
**GET** `/users/friend-requests/incoming/{userId}`

Path parameter:
- `userId`: ID của user muốn xem lời mời

Response:
```json
{
  "result": {
    "userId": "user123",
    "incomingFriendRequests": ["user456", "user789", "user101"]
  }
}
```

---

### 5. **[NEW]** Lấy danh sách những lời mời kết bạn mà mình đã gửi đi
**GET** `/users/friend-requests/outgoing/{userId}`

Path parameter:
- `userId`: ID của user muốn xem lời mời

Response:
```json
{
  "result": {
    "userId": "user123",
    "outgoingFriendRequests": ["user456", "user789"]
  }
}
```

---

### 6. Xóa bạn
**POST** `/users/friendships/remove`

Request body:
```json
{
  "userId": "user123",
  "friendId": "user456"
}
```

Response:
```json
{
  "result": "Friend removed"
}
```

---

### 7. Lấy tất cả mối quan hệ của user
**GET** `/users/all-relationship/{userId}`

Path parameter:
- `userId`: ID của user muốn xem mối quan hệ

Response:
```json
{
  "result": {
    "userId": "user123",
    "friends": ["user456", "user789"],
    "blocked": ["user101"],
    "dating": [],
    "family": ["user999"],
    "colleagues": ["user111", "user222"],
    "following": ["user333"],
    "closeFriends": ["user456"],
    "anotherUserFollowedIt": ["user444", "user555"],
    "incomingFriendRequests": ["user666"],
    "outgoingFriendRequests": ["user777"]
  }
}
```

---

## Thay đổi mới
- **Endpoint 4**: `GET /users/friend-requests/incoming/{userId}` - Lấy danh sách những người gửi lời mời kết bạn
- **Endpoint 5**: `GET /users/friend-requests/outgoing/{userId}` - Lấy danh sách lời mời kết bạn mình gửi
- **DTO mới**: `IncomingFriendRequestsResponse`, `OutgoingFriendRequestsResponse` - Để response riêng biệt
- **Service methods mới**: `getIncomingFriendRequests()`, `getOutgoingFriendRequests()` - Logic xử lý

## Ghi chú
- Lời mời kết bạn có status `PENDING`
- Bạn bè có status `ACTIVE`
- Endpoint mới giúp tối ưu bandwidth so với việc gọi `/all-relationship` để chỉ lấy incoming/outgoing requests
