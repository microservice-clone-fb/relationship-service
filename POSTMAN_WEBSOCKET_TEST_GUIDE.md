# Postman WebSocket Test Guide - Friend Request Realtime

## 📋 Tổng quan

Collection này test **realtime friend request** qua Socket.IO với **chỉ 1 Postman**:

### ✅ Cách đơn giản nhất (Khuyến nghị):
- **Postman**: Dùng REST API để gửi friend request (User A)
- **Expo Go trên điện thoại**: Đã connect Socket.IO sẵn, sẽ nhận event realtime (User B)
- **Kết quả**: Khi Postman gọi REST API → Backend emit event → Điện thoại nhận ngay lập tức

### 🔧 Cách nâng cao (Nếu muốn test WebSocket):
- **1 Postman Web + 1 Postman Desktop App** ✅ (Khuyến nghị cho WebSocket test)
  - Postman Web: Connect User B để listen events
  - Postman Desktop: Connect User A để emit events
- Hoặc mở 2 Postman Desktop instances (2 cửa sổ)
- Hoặc dùng REST API trong Postman + WebSocket trong Postman (nhưng chỉ listen được, không thể vừa emit vừa listen cùng lúc)

## 🚀 Setup

### 1. Start Services

```bash
# Terminal 1: Identity Service
cd NewBE/identity-service
mvn spring-boot:run

# Terminal 2: Relationship Service (HTTP + Socket.IO)
cd NewBE/relationship-service
mvn spring-boot:run
```

**Kiểm tra:**
- Identity service: `http://localhost:5001/identity/auth/login`
- Relationship service HTTP: `http://localhost:5007/relationship/users/all-relationship/{userId}`
- Socket.IO: `ws://localhost:9092` (sẽ connect qua Postman)

### 2. Import Collection vào Postman

1. Mở Postman (version 10.0+ để hỗ trợ WebSocket)
2. Click **Import** → Chọn file `FriendRequest_Realtime_Test.postman_collection.json`
3. Collection sẽ xuất hiện trong sidebar

### 3. Cấu hình Variables

Collection đã có sẵn variables, nhưng bạn có thể chỉnh nếu cần:

- `base_url`: `http://localhost:5000/v1` (API Gateway) hoặc `http://localhost:5007/relationship` (direct)
- `socketio_host`: `localhost` (hoặc IP máy tính nếu test từ điện thoại)
- `socketio_port`: `9092`

## 🎯 Test Realtime với 1 Postman (Cách đơn giản nhất)

### Setup nhanh:
1. **Expo Go trên điện thoại**: Đã login User B và đang mở app (socket tự động connect)
2. **Postman**: Chỉ cần dùng REST API, không cần WebSocket

### Flow test:
```
Postman (User A)              Backend              Expo Go (User B)
     |                            |                        |
     |-- POST /friend-requests/send -->|                    |
     |                            |-- Save DB              |
     |<-- 200 OK ----------------|                        |
     |                            |-- Emit friendRequest:received |
     |                            |----------------------->|
     |                            |                        |-- Badge xanh hiện
     |                            |                        |-- List refresh realtime
```

### Các bước:
1. **Login User A trong Postman** → Lấy token và userId
2. **Login User B trong Expo Go** → Đảm bảo socket đã connect (check log trong app)
3. **Postman gọi REST API**: `POST /relationship/users/friend-requests/send`
   - Body: `{ "requesterId": "{userA_userId}", "targetUserId": "{userB_userId}", "message": "Hi!" }`
4. **Kiểm tra điện thoại**: Badge xanh hiện ngay, danh sách friend requests tự động refresh

**✅ Đây là cách test realtime đơn giản nhất - không cần WebSocket trong Postman!**

### 🖥️ Cách test WebSocket với 2 Postman Desktop App (Mở 2 cửa sổ):

**Cách mở 2 cửa sổ Postman Desktop:**
1. Mở Postman Desktop App lần đầu (cửa sổ 1)
2. Mở lại Postman Desktop App lần nữa (cửa sổ 2) - hoặc dùng shortcut:
   - **Windows**: Click icon Postman trên taskbar → Click chuột phải → "New Window"
   - **Mac**: `Cmd + N` hoặc `File → New Window`

**Setup test:**
1. **Cửa sổ 1 - Postman Desktop (User A - Emit)**:
   - Login User A → Lấy token và userId
   - Tạo WebSocket request: `ws://localhost:9092/?userId={userA_userId}&token={userA_token}`
   - Click **Connect** → Status: **Connected**
   - Emit event: `friendRequest:send` với payload:
     ```json
     {
       "targetUserId": "{userB_userId}",
       "message": "Hi, let's be friends!"
     }
     ```

2. **Cửa sổ 2 - Postman Desktop (User B - Listen)**:
   - Login User B → Lấy token và userId
   - Tạo WebSocket request: `ws://localhost:9092/?userId={userB_userId}&token={userB_token}`
   - Click **Connect** → Status: **Connected**
   - Để mở tab listen, sẽ tự động nhận events

3. **Kết quả**:
   - Cửa sổ 1 (User A) nhận ACK: `{ "success": true, "message": "Friend request sent successfully", "code": 1000 }`
   - Cửa sổ 2 (User B) nhận event realtime: `friendRequest:received` với data đầy đủ

**✅ Cách này cho phép test WebSocket đầy đủ với 2 Postman Desktop instances!**

### 🌐 Alternative: 1 Postman Web + 1 Postman Desktop App:

1. **Postman Web (trình duyệt)**: 
   - Login User B → Connect WebSocket để listen events
   - URL: `ws://localhost:9092/?userId={userB_userId}&token={userB_token}`
   - Sau khi connect, sẽ thấy status: **Connected**

2. **Postman Desktop App**:
   - Login User A → Connect WebSocket để emit events
   - URL: `ws://localhost:9092/?userId={userA_userId}&token={userA_token}`
   - Emit event: `friendRequest:send` với payload `{ "targetUserId": "{userB_userId}", "message": "Hi!" }`

3. **Kết quả**:
   - Postman Desktop nhận ACK: `{ "success": true, "message": "Friend request sent successfully" }`
   - Postman Web nhận event realtime: `friendRequest:received` với data đầy đủ

---

## 📝 Test Flow (Chi tiết)

### Bước 1: Login 2 Users

1. **Login - User A (Requester)**
   - Chạy request → Token và userId tự động lưu vào `userA_token`, `userA_userId`
   - Kiểm tra console log: `✅ User A logged in: {userId}`

2. **Login - User B (Target)**
   - Chạy request → Token và userId tự động lưu vào `userB_token`, `userB_userId`
   - Kiểm tra console log: `✅ User B logged in: {userId}`

### Bước 2: Test REST API (Optional)

Có thể test REST API trước để đảm bảo logic hoạt động:
- **Send Friend Request (REST)**: User A gửi lời mời cho User B
- **Get All Relationships**: Kiểm tra relationship status
- **Accept/Reject/Cancel/Unfriend**: Test các actions khác

### Bước 3: Test WebSocket Realtime (Nâng cao)

**⚠️ Lưu ý:** Với 1 Postman, bạn **KHÔNG THỂ** vừa emit vừa listen cùng lúc vì:
- Postman chỉ cho phép 1 WebSocket connection/tab
- Nếu connect User A để emit → không thể connect User B để listen
- Nếu connect User B để listen → không thể connect User A để emit

**Giải pháp:**
1. **Dùng REST API** (khuyến nghị) - Xem section "Test Realtime với 1 Postman" ở trên
2. **Mở 2 Postman instances** (2 cửa sổ) - 1 để emit, 1 để listen
3. **Postman + Expo Go** - Postman dùng REST API, Expo Go listen socket

#### 3.1. Connect WebSocket - User B (Listen)

1. Mở request **"Connect WebSocket - User B (Listen for incoming requests)"**
2. Click **Connect** (Postman sẽ connect tới `ws://localhost:9092/?userId={userB_userId}&token={userB_token}`)
3. **Lưu ý:** Postman WebSocket cần format đúng:
   - URL: `ws://localhost:9092/?userId=xxx&token=xxx`
   - Sau khi connect, bạn sẽ thấy connection status: **Connected**

#### 3.2. Emit friendRequest:send (User A → User B)

1. Mở request **"Emit: friendRequest:send (User A → User B)"**
2. Click **Connect** (User A connect tới socket)
3. Sau khi connected, trong phần **Message** của Postman WebSocket:
   - **Event name:** `friendRequest:send`
   - **Payload:**
     ```json
     {
       "targetUserId": "{{userB_userId}}",
       "message": "Hi, let's be friends!"
     }
     ```
4. Click **Send**
5. **Expected Results:**
   - **ACK Response** (trong Postman User A):
     ```json
     {
       "success": true,
       "message": "Friend request sent successfully",
       "code": 1000
     }
     ```
   - **Realtime Event** (trong Postman User B - đang listen):
     ```json
     {
       "event": "friendRequest:received",
       "data": {
         "type": "FRIEND_REQUEST_SENT",
         "requesterId": "{userA_userId}",
         "targetUserId": "{userB_userId}",
         "requesterName": null
       }
     }
     ```
   - **Trên điện thoại (Expo Go):** Badge xanh hiển thị ngay lập tức, danh sách friend requests tự động refresh

#### 3.3. Test các Events khác

Tương tự, bạn có thể test:

- **friendRequest:cancel** - User A hủy lời mời → User B nhận `friendRequest:cancelled`
- **friendRequest:accept** - User B chấp nhận → Cả 2 users nhận `friendRequest:accepted`
- **friendRequest:reject** - User B từ chối → User A nhận `friendRequest:rejected`
- **friendship:remove** - User A xóa bạn → User B nhận `friendship:removed`

## 🔍 Debug Tips

### 1. Kiểm tra Socket.IO Server Logs

Trong terminal chạy `relationship-service`, bạn sẽ thấy:
```
🔌 Registering Socket.IO relationship event listeners
✅ Socket client connected - Session ID: {sessionId}, User ID: {userId}
📤 Emitted event 'friendRequest:received' to user: {targetUserId}
```

### 2. Postman WebSocket không connect?

- **Check version:** Postman 10.0+ mới hỗ trợ WebSocket
- **Check URL format:** Phải là `ws://` không phải `http://`
- **Check query params:** `userId` và `token` phải có trong URL
- **Check firewall:** Port 9092 phải mở

### 3. Không nhận được realtime event?

- **Check connection:** Cả 2 WebSocket connections phải **Connected**
- **Check userId:** User B phải đúng `targetUserId` trong payload
- **Check server logs:** Xem có emit event không
- **Check event name:** Phải đúng `friendRequest:received` (không phải `friendRequest:send`)

### 4. Test với Expo Go trên điện thoại

1. **Setup ngrok** (nếu khác mạng):
   ```bash
   ngrok http 5000  # API Gateway
   ngrok http 9092  # Socket.IO
   ```

2. **Update variables trong Postman:**
   - `base_url`: `https://xxxx.ngrok-free.app/v1`
   - `socketio_host`: `xxxx.ngrok-free.app` (không có `https://`)
   - `socketio_port`: `443` (hoặc port ngrok cho WebSocket)

3. **Update `axiosConfig.ts` và `socketService.ts`** trong Face app với ngrok URLs

## 📊 Event Flow Diagram

```
User A (Postman)                    Backend                    User B (Postman/Expo Go)
     |                                  |                              |
     |-- friendRequest:send ---------->|                              |
     |                                  |-- Process request            |
     |                                  |-- Save to DB                |
     |<-- ACK: success ----------------|                              |
     |                                  |-- Emit friendRequest:received|
     |                                  |--------------------------->|
     |                                  |                              |-- Badge xanh hiện
     |                                  |                              |-- List refresh
```

## 🎯 Quick Test Checklist

### Với 1 Postman + Expo Go (Khuyến nghị):
- [ ] Identity service chạy (port 5001)
- [ ] Relationship service chạy (port 5007 + 9092)
- [ ] Login User A trong Postman → có token và userId
- [ ] Login User B trong Expo Go → socket tự động connect
- [ ] Postman gọi REST API: `POST /friend-requests/send`
- [ ] Expo Go nhận event realtime → Badge xanh hiện, list refresh

### Với 1 Postman Web + 1 Postman Desktop App (Test WebSocket):
- [ ] Identity service chạy (port 5001)
- [ ] Relationship service chạy (port 5007 + 9092)
- [ ] Login User A trong Postman Desktop → có token và userId
- [ ] Login User B trong Postman Web → có token và userId
- [ ] Connect WebSocket User B trong Postman Web → Connected
- [ ] Connect WebSocket User A trong Postman Desktop → Connected
- [ ] Emit friendRequest:send trong Postman Desktop → nhận ACK success
- [ ] Postman Web nhận event friendRequest:received realtime

### Với 2 Postman Desktop instances (Alternative):
- [ ] Identity service chạy (port 5001)
- [ ] Relationship service chạy (port 5007 + 9092)
- [ ] Login User A trong Postman Desktop 1 → có token và userId
- [ ] Login User B trong Postman Desktop 2 → có token và userId
- [ ] Connect WebSocket User B trong Postman Desktop 2 → Connected
- [ ] Connect WebSocket User A trong Postman Desktop 1 → Connected
- [ ] Emit friendRequest:send trong Postman Desktop 1 → nhận ACK success
- [ ] Postman Desktop 2 nhận event friendRequest:received realtime

## 📚 Tham khảo

- Socket.IO Events: `RelationshipSocketEventHandler.java`
- REST API: `UserRelationshipController.java`
- Frontend Integration: `Face/services/socketService.ts`

