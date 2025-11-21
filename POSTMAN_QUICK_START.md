# 🚀 Postman Quick Start - Test Realtime Friend Request

## 📥 Import Collection vào Postman

1. Mở **Postman Desktop App**
2. Click **Import** (góc trên bên trái)
3. Chọn file `FriendRequest_Realtime_Test.postman_collection.json`
4. Click **Import** → Collection sẽ xuất hiện trong sidebar

## 🖥️ Setup 2 Cửa sổ Postman

### Mở cửa sổ thứ 2:
- **Windows**: Click icon Postman trên taskbar → Click chuột phải → "New Window"
- **Mac**: `Cmd + N` hoặc `File → New Window`

---

## 👤 CỬA SỔ 1: User A (Người gửi lời mời)

### Bước 1: Login User A
1. Mở collection **"Friend Request Realtime Test"**
2. Vào folder **"1. Authentication"**
3. Chạy request **"Login - User A (Requester)"**
4. Body mặc định:
   ```json
   {
     "username": "userA",
     "password": "password123"
   }
   ```
5. Click **Send**
6. ✅ Kiểm tra Console: `✅ User A logged in: {userId}`
7. Token và userId tự động lưu vào variables

### Bước 2: Connect WebSocket (User A)
1. Vào folder **"3. WebSocket - Realtime Events"**
2. Mở request **"Emit: friendRequest:send (User A → User B)"**
3. URL đã tự động fill: `ws://localhost:9092/?userId={{userA_userId}}&token={{userA_token}}`
4. Click **Connect** → Đợi status: **Connected** ✅

### Bước 3: Emit friendRequest:send
1. Sau khi **Connected**, trong phần **Message**:
   - **Event name:** `friendRequest:send`
   - **Payload:**
     ```json
     {
       "targetUserId": "{{userB_userId}}",
       "message": "Hi, let's be friends!"
     }
     ```
   - ⚠️ **Lưu ý**: Thay `{{userB_userId}}` bằng userId thực tế của User B (copy từ cửa sổ 2)
2. Click **Send**
3. ✅ **Expected**: Nhận ACK response:
   ```json
   {
     "success": true,
     "message": "Friend request sent successfully",
     "code": 1000
   }
   ```

---

## 👤 CỬA SỔ 2: User B (Người nhận lời mời)

### Bước 1: Login User B
1. Mở collection **"Friend Request Realtime Test"** (cùng collection)
2. Vào folder **"1. Authentication"**
3. Chạy request **"Login - User B (Target)"**
4. Body mặc định:
   ```json
   {
     "username": "userB",
     "password": "password123"
   }
   ```
5. Click **Send**
6. ✅ Kiểm tra Console: `✅ User B logged in: {userId}`
7. **Copy userId này** → Paste vào cửa sổ 1 (thay `{{userB_userId}}`)

### Bước 2: Connect WebSocket (User B)
1. Vào folder **"3. WebSocket - Realtime Events"**
2. Mở request **"Connect WebSocket - User B (Listen for incoming requests)"**
3. URL đã tự động fill: `ws://localhost:9092/?userId={{userB_userId}}&token={{userB_token}}`
4. Click **Connect** → Đợi status: **Connected** ✅
5. **Giữ connection này mở** để listen events

### Bước 3: Nhận Event Realtime (Tự động!)
1. Sau khi User A emit `friendRequest:send` (ở cửa sổ 1)
2. ✅ **Backend tự động emit event cho User B** - Không cần làm gì thêm!
3. ✅ **Cửa sổ 2 (User B) sẽ tự động nhận event realtime ngay lập tức:**
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
4. Event sẽ xuất hiện trong phần **Messages** của WebSocket connection

**💡 Lưu ý:** User B chỉ cần connect WebSocket và listen - Event sẽ tự động đến, không cần emit gì cả!

---

## ⚡ Flow Realtime Tự Động

**Khi User A gửi friend request (qua WebSocket hoặc REST API):**

```
User A (Postman)              Backend                    User B (Postman/Expo Go)
     |                            |                              |
     |-- friendRequest:send ---->|                              |
     |                            |-- Xử lý request              |
     |                            |-- Lưu vào database           |
     |<-- ACK: success ----------|                              |
     |                            |-- TỰ ĐỘNG emit event         |
     |                            |   friendRequest:received      |
     |                            |--------------------------->|
     |                            |                              |-- Event đến realtime!
     |                            |                              |-- Badge xanh hiện
     |                            |                              |-- List refresh
```

**✅ Điểm quan trọng:**
- User B **KHÔNG CẦN** làm gì cả - chỉ cần connect WebSocket và listen
- Backend **TỰ ĐỘNG** emit event cho User B sau khi xử lý request
- Event đến **NGAY LẬP TỨC** (realtime) - không cần refresh hay polling

---

## 🎯 Test Flow Hoàn Chỉnh

### Scenario 1: Gửi lời mời kết bạn
```
1. Cửa sổ 1: Login User A → Connect WebSocket → Emit friendRequest:send
2. Cửa sổ 2: Login User B → Connect WebSocket → Listen
3. Kết quả: Cửa sổ 2 nhận event friendRequest:received realtime
```

### Scenario 2: Hủy lời mời
```
1. Cửa sổ 1: Emit friendRequest:cancel
2. Cửa sổ 2: Nhận event friendRequest:cancelled
```

### Scenario 3: Chấp nhận lời mời
```
1. Cửa sổ 2: Emit friendRequest:accept
2. Cả 2 cửa sổ: Nhận event friendRequest:accepted
```

### Scenario 4: Từ chối lời mời
```
1. Cửa sổ 2: Emit friendRequest:reject
2. Cửa sổ 1: Nhận event friendRequest:rejected
```

### Scenario 5: Xóa bạn bè
```
1. Cửa sổ 1: Emit friendship:remove
2. Cửa sổ 2: Nhận event friendship:removed
```

---

## 🔧 Troubleshooting

### Variables không tự động fill?
- Kiểm tra: Sau khi login, mở **Variables** tab trong collection
- Xem có `userA_token`, `userA_userId`, `userB_token`, `userB_userId` không
- Nếu không có, chạy lại login request

### WebSocket không connect?
- Check service đang chạy: `relationship-service` port 9092
- Check URL format: `ws://localhost:9092/?userId=xxx&token=xxx`
- Check token và userId có đúng không

### Không nhận được event?
- Check cả 2 WebSocket connections đều **Connected**
- Check `targetUserId` trong payload có đúng `userB_userId` không
- Check server logs xem có emit event không

### Lỗi "userId not found"?
- Đảm bảo đã login và variables đã được set
- Copy userId từ cửa sổ 2 → Paste vào cửa sổ 1 thay vì dùng `{{userB_userId}}`

---

## 📝 Checklist Nhanh

**Cửa sổ 1 (User A):**
- [ ] Login User A → Có token và userId
- [ ] Connect WebSocket → Connected
- [ ] Copy `userB_userId` từ cửa sổ 2
- [ ] Emit `friendRequest:send` với `targetUserId` = `userB_userId`
- [ ] Nhận ACK success

**Cửa sổ 2 (User B):**
- [ ] Login User B → Có token và userId
- [ ] Copy `userB_userId` → Gửi cho cửa sổ 1
- [ ] Connect WebSocket → Connected
- [ ] Listen events → Nhận `friendRequest:received`

---

## 💡 Tips

1. **Dùng REST API thay vì WebSocket** (đơn giản hơn):
   - Cửa sổ 1: Gọi `POST /friend-requests/send` (REST API)
   - Cửa sổ 2: Vẫn connect WebSocket để listen
   - Kết quả giống nhau nhưng không cần emit WebSocket

2. **Test với Expo Go trên điện thoại**:
   - Cửa sổ 1: Postman gọi REST API
   - Điện thoại: Expo Go tự động listen socket
   - Kết quả: Điện thoại nhận realtime event

3. **Copy userId nhanh**:
   - Sau khi login, mở **Variables** tab
   - Copy `userB_userId` → Paste vào cửa sổ 1

