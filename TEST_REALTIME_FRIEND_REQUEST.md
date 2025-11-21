# 🧪 Test Realtime Friend Request - Postman → Expo Go

## 📋 Mục đích
Test xem app Expo Go có nhận được realtime friend request khi gửi từ Postman không.

## 🎯 Flow Test

```
Postman (User B)                    Expo Go (User A - tam/taki)
     |                                      |
     |-- Login User B                      |-- Login User A
     |-- Lấy userId của User B             |-- Lấy userId của User A
     |                                      |-- Connect Socket.IO
     |                                      |-- Listen events...
     |-- Gửi friend request ──────────────>|-- Backend emit event
     |   đến User A                        |-- Nhận friendRequest:received
     |                                      |-- Log trong console
```

---

## 📱 BƯỚC 1: Setup Expo Go (User A - tam hoặc taki)

### 1.1. Login trên Expo Go
1. Mở app trên Expo Go
2. Login với user **tam** hoặc **taki**
3. Đảm bảo app đã connect Socket.IO thành công
   - Kiểm tra log: `✅ Socket connected! ID: ...`

### 1.2. Lấy userId của User A
**Cách 1: Từ AsyncStorage (Debug)**
- Mở React Native Debugger hoặc Metro logs
- Tìm log: `👤 User ID: {userId}`
- Hoặc check AsyncStorage: `userId`

**Cách 2: Từ Login Response**
- Khi login, response có `result.userId`
- Copy userId này → **Lưu lại để dùng ở Bước 2**

### 1.3. Kiểm tra Socket Connection
Mở Metro logs hoặc React Native Debugger, tìm các log:
```
✅ Socket connected! ID: abc123
🔌 [Socket] Connecting to Gateway Socket.IO server at: http://192.168.1.20:9092
```

**⚠️ QUAN TRỌNG:** Socket phải **Connected** thì mới nhận được realtime events!

### 1.4. Mở Log Console
- **Metro Bundler**: Xem logs trong terminal chạy `npm start`
- **React Native Debugger**: Mở DevTools → Console tab
- **Expo Go**: Shake device → "Debug Remote JS" → Mở Chrome DevTools

**Các log cần theo dõi:**
- `🔔 [SOCKET] Friend request received event:`
- `📤 [SOCKET] Emitting to eventEmitter immediately...`
- `✅ [SOCKET] Event emitted to eventEmitter - UI should update immediately`

---

## 🖥️ BƯỚC 2: Setup Postman (User B)

### 2.1. Login User B
**Endpoint:** `POST http://localhost:5000/v1/identity/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "username": "test182@gmail.com",
  "password": "123456"
}
```

**Response:**
```json
{
  "code": 1000,
  "message": "Login successful",
  "result": {
    "userId": "user-b-id-here",
    "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    ...
  }
}
```

**Lưu lại:**
- ✅ `result.token` → Dùng cho Authorization header
- ✅ `result.userId` → Dùng để verify (không cần cho request này)

### 2.2. Lấy userId của User A (từ Bước 1.2)
- Copy `userId` của User A (tam hoặc taki) từ Expo Go
- **Lưu lại** → Dùng làm `targetUserId` ở bước tiếp theo

---

## 📤 BƯỚC 3: Gửi Friend Request từ Postman

### 3.1. Gửi Friend Request
**Endpoint:** `POST http://localhost:5000/v1/identity/relationships/friend-requests`

**Headers:**
```
Authorization: Bearer {token_từ_bước_2.1}
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "targetUserId": "{userId_của_User_A_từ_bước_1.2}",
  "message": "Xin chào! Mình muốn kết bạn với bạn."
}
```

**Ví dụ:**
```json
{
  "targetUserId": "5f1653ac-65ed-4a6f-a860-1d55ca60c778",
  "message": "Test realtime friend request"
}
```

### 3.2. Response mong đợi
```json
{
  "code": 1000,
  "message": "Friend request sent",
  "result": "Friend request sent"
}
```

**✅ Nếu nhận được response này → Backend đã xử lý và sẽ emit event cho User A**

---

## 🔍 BƯỚC 4: Kiểm tra Log trên Expo Go

### 4.1. Logs cần tìm trong Metro/Console

**Khi nhận được realtime event, bạn sẽ thấy:**

```
🔔 [SOCKET] Friend request received event: {
  "requesterId": "user-b-id-here",
  "requesterName": null,
  "targetUserId": "user-a-id-here"
}
📤 [SOCKET] Emitting to eventEmitter immediately...
✅ [SOCKET] Event emitted to eventEmitter - UI should update immediately
```

**Các log tiếp theo (từ các screens listen event):**
```
🔔 [NOTIFICATIONS] Friend request received event - refreshing IMMEDIATELY...
🔔 [BADGE] Friend request received event - updating count IMMEDIATELY...
```

### 4.2. Kiểm tra UI Update
- **Notifications Screen**: Badge count tăng lên
- **Friend Requests Screen**: Danh sách friend requests tự động refresh
- **Badge**: Số lượng friend requests chưa đọc tăng lên

---

## ✅ Checklist Test

### Setup Expo Go
- [ ] Login thành công với user tam hoặc taki
- [ ] Socket.IO đã connect (log: `✅ Socket connected!`)
- [ ] Đã lấy được `userId` của User A
- [ ] Đã mở log console (Metro/React Native Debugger)

### Setup Postman
- [ ] Login User B thành công
- [ ] Đã lưu `token` từ login response
- [ ] Đã có `userId` của User A (từ Expo Go)

### Gửi Friend Request
- [ ] Gửi POST request đến `/identity/relationships/friend-requests`
- [ ] Request thành công (code 1000)
- [ ] Body có `targetUserId` = userId của User A

### Kiểm tra Realtime
- [ ] Expo Go nhận được log: `🔔 [SOCKET] Friend request received event`
- [ ] Event có đúng `requesterId` = userId của User B
- [ ] Event có đúng `targetUserId` = userId của User A
- [ ] UI tự động update (badge, notifications screen)

---

## 🐛 Troubleshooting

### ❌ Expo Go không nhận được event?

**1. Kiểm tra Socket Connection**
```
Log cần có: ✅ Socket connected! ID: ...
```
- Nếu không có → Socket chưa connect
- **Giải pháp**: 
  - Restart app
  - Kiểm tra backend URL trong `axiosConfig.ts`
  - Kiểm tra Socket.IO server có chạy không (port 9092)

**2. Kiểm tra Backend có emit event không**
- Xem logs của `relationship-service`
- Tìm log: `Emitting friendRequest:received to user: {userId}`
- Nếu không có → Backend chưa emit event

**3. Kiểm tra userId có đúng không**
- Verify `targetUserId` trong Postman request = `userId` của User A trên Expo Go
- Verify `requesterId` trong event = `userId` của User B

**4. Kiểm tra Network**
- Expo Go và Postman phải cùng network
- Backend URL phải accessible từ cả 2 devices
- Nếu dùng ngrok → Đảm bảo URL đúng

### ❌ Postman request thất bại?

**1. Lỗi 401 Unauthorized**
- Token đã hết hạn → Login lại
- Header format sai → `Authorization: Bearer {token}` (có dấu cách)

**2. Lỗi 400 Bad Request**
- `targetUserId` không hợp lệ → Kiểm tra format UUID
- Body JSON sai format → Kiểm tra syntax

**3. Lỗi 500 Internal Server Error**
- Xem logs của `identity-service` và `relationship-service`
- Kiểm tra database connection

### ❌ Socket không connect?

**1. Kiểm tra Backend URL**
- File: `Face/services/axiosConfig.ts`
- `BACKEND_URL` phải đúng với Gateway URL
- Socket.IO tự động parse từ `BACKEND_URL` → `http://{host}:9092`

**2. Kiểm tra Port 9092**
- Gateway Socket.IO chạy trên port 9092
- Đảm bảo port không bị firewall block

**3. Kiểm tra Token và userId**
- Socket cần `token` và `userId` từ AsyncStorage
- Nếu thiếu → Login lại trên app

---

## 📝 Notes

### IDs của tam và taki
- **tam**: `{userId_tam}` (lấy từ Expo Go sau khi login)
- **taki**: `{userId_taki}` (lấy từ Expo Go sau khi login)

### Backend URL
- **Gateway HTTP**: `http://localhost:5000/v1` (hoặc IP WiFi)
- **Gateway Socket.IO**: `http://localhost:9092` (hoặc IP WiFi)
- **File config**: `Face/services/axiosConfig.ts`

### Event Flow
1. Postman gửi POST `/identity/relationships/friend-requests`
2. Identity Service xử lý → Gọi Relationship Service
3. Relationship Service lưu vào DB → Emit Socket.IO event `friendRequest:received`
4. Gateway Socket.IO forward event đến User A (nếu đang connect)
5. Expo Go nhận event → Emit qua eventEmitter → UI update

---

## 🎉 Kết quả mong đợi

Khi test thành công:
- ✅ Postman nhận response: `{ "code": 1000, "message": "Friend request sent" }`
- ✅ Expo Go nhận log: `🔔 [SOCKET] Friend request received event: {...}`
- ✅ UI tự động update: Badge count tăng, Notifications screen refresh
- ✅ Friend Requests screen hiển thị request mới

**🎊 Realtime hoạt động hoàn hảo!**

