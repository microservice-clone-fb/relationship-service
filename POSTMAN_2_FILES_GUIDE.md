# 🚀 Hướng Dẫn Test Realtime với 2 File JSON

## 📥 Import 2 Collections

1. **Mở Postman Desktop App**
2. **Import 2 files:**
   - `FriendRequest_UserA_Emit.postman_collection.json` → Collection cho User A
   - `FriendRequest_UserB_Listen.postman_collection.json` → Collection cho User B

---

## 🖥️ Setup 2 Cửa sổ Postman

### Mở cửa sổ thứ 2:
- **Windows**: Click icon Postman trên taskbar → Click chuột phải → "New Window"
- **Mac**: `Cmd + N` hoặc `File → New Window`

---

## 👤 CỬA SỔ 1: User B (Listen Realtime)

**Credentials đã được set sẵn:**
- Username: `test181@gmail.com`
- Password: `123456`

### Bước 1: Import Collection
1. Import `FriendRequest_UserB_Listen.postman_collection.json`
2. Collection sẽ xuất hiện: **"Friend Request - User B (Listen Realtime)"**

### Bước 2: Chạy Auto Request
1. Mở collection → Chọn request **"🚀 Auto: Listen Realtime"**
2. Click **Send** → Tự động:
   - ✅ Login User B (test181@gmail.com)
   - ✅ Connect WebSocket
   - ✅ Sẵn sàng nhận events realtime

### Bước 3: Copy userId
1. Mở **Console** (View → Show Postman Console)
2. Tìm dòng: `📋 ⚠️ QUAN TRỌNG: Copy userId này và gửi cho User A: {userId}`
3. **Copy userId** → Gửi cho cửa sổ 2 (User A)

### Bước 4: Giữ WebSocket Connection mở
- Connection sẽ hiển thị status: **Connected** ✅
- **Giữ connection này mở** để nhận events realtime

---

## 👤 CỬA SỔ 2: User A (Emit Friend Request)

**Credentials đã được set sẵn:**
- Username: `test182@gmail.com`
- Password: `123456`

### Bước 1: Import Collection
1. Import `FriendRequest_UserA_Emit.postman_collection.json`
2. Collection sẽ xuất hiện: **"Friend Request - User A (Emit)"**

### Bước 2: Set target_user_id
1. Mở collection → Click **Variables** tab
2. Tìm variable `target_user_id`
3. **Paste userId từ User B** (copy từ cửa sổ 1)
4. Click **Save**

### Bước 3: Chạy Auto Request
1. Mở collection → Chọn request **"🚀 Auto: Send Friend Request"**
2. Click **Send** → Tự động:
   - ✅ Login User A (test182@gmail.com)
   - ✅ Connect WebSocket
   - ✅ Emit `friendRequest:send`
   - ✅ Hiển thị ACK response

### Bước 4: Kiểm tra kết quả
- **Cửa sổ 2 (User A)**: Nhận ACK `{ "success": true, "message": "Friend request sent successfully" }`
- **Cửa sổ 1 (User B)**: Tự động nhận event `friendRequest:received` realtime! 🎉

---

## 🎯 Flow Hoàn Chỉnh

```
Cửa sổ 1 (User B)              Cửa sổ 2 (User A)
     |                              |
     |-- Import collection B        |-- Import collection A
     |-- Chạy "Auto: Listen"        |-- Set target_user_id = userId từ User B
     |-- Copy userId ──────────────>|-- Chạy "Auto: Send"
     |-- Connect WebSocket           |-- Connect WebSocket
     |-- Listen events...            |-- Emit friendRequest:send
     |                              |-- Nhận ACK success
     |<-- friendRequest:received ────| (Backend tự động emit)
     |   (Realtime event!)          |
```

---

## ✅ Checklist Nhanh

**Cửa sổ 1 (User B):**
- [ ] Import `FriendRequest_UserB_Listen.postman_collection.json`
- [ ] Chạy **"🚀 Auto: Listen Realtime"**
- [ ] Copy `userB_userId` từ Console
- [ ] Gửi userId cho cửa sổ 2
- [ ] Giữ WebSocket connection mở

**Cửa sổ 2 (User A):**
- [ ] Import `FriendRequest_UserA_Emit.postman_collection.json`
- [ ] Set variable `target_user_id` = userId từ User B
- [ ] Chạy **"🚀 Auto: Send Friend Request"**
- [ ] Nhận ACK success
- [ ] Kiểm tra cửa sổ 1 nhận event realtime

---

## 🔧 Troubleshooting

### Lỗi "Please set 'target_user_id' variable first"
- **Giải pháp**: Mở collection User A → Variables tab → Set `target_user_id` = userId từ User B

### User B không nhận được event?
- **Check**: WebSocket connection của User B phải **Connected**
- **Check**: `target_user_id` trong User A có đúng `userB_userId` không
- **Check**: Server logs xem có emit event không

### Variables không tự động fill?
- **Giải pháp**: Chạy lại request "Auto: Listen" hoặc "Auto: Send"
- **Check**: Console để xem có lỗi gì không

### WebSocket không connect?
- **Check**: Service đang chạy: `relationship-service` port 9092
- **Check**: URL format: `ws://localhost:9092/?userId=xxx&token=xxx`
- **Check**: Token và userId có đúng không

---

## 💡 Tips

1. **Dùng Console để debug**: View → Show Postman Console
2. **Copy userId nhanh**: Click vào variable `userB_userId` trong Variables tab → Copy
3. **Test nhiều lần**: Có thể chạy lại "Auto: Send" nhiều lần để test
4. **Check server logs**: Xem terminal chạy `relationship-service` để debug

---

## 📝 Manual Requests (Nếu cần)

Nếu auto request không hoạt động, có thể chạy manual:

**User B:**
1. "Manual: Login User B" → Lấy token và userId
2. "Manual: Connect WebSocket (User B)" → Connect và listen

**User A:**
1. "Manual: Login User A" → Lấy token và userId
2. Set `target_user_id` = userId từ User B
3. "Manual: Connect WebSocket (User A)" → Connect và emit event

---

## 🎉 Kết Quả Mong Đợi

Khi User A gửi friend request:
- ✅ **User A**: Nhận ACK `{ "success": true, "code": 1000 }`
- ✅ **User B**: Tự động nhận event `friendRequest:received` realtime
- ✅ **Event data**:
  ```json
  {
    "type": "FRIEND_REQUEST_SENT",
    "requesterId": "{userA_userId}",
    "targetUserId": "{userB_userId}",
    "requesterName": null
  }
  ```

**🎊 Realtime hoạt động hoàn hảo!**

