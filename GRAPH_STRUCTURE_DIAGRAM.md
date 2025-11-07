# Neo4j Graph Database Structure Diagram

## Sơ đồ tổng quan các mối quan hệ

```
                            ┌─────────────────┐
                            │      USER       │
                            └────────┬────────┘
                                     │
        ┌────────────────────────────┼────────────────────────────┐
        │                            │                            │
        │                            │                            │
        ▼                            ▼                            ▼
┌───────────────┐          ┌───────────────┐          ┌───────────────┐
│  USER ↔ USER  │          │  USER → PAGE  │          │  USER → GROUP │
│  Relationships │          │  Relationships │          │  Relationships │
└───────────────┘          └───────────────┘          └───────────────┘
        │                            │                            │
        │                            │                            │
        ▼                            ▼                            ▼
```

### Chi tiết User ↔ User Relationships

```
      USER ──────────────────────► USER
        │
        ├── FRIEND_WITH (bạn bè) ──────────── Bidirectional
        │   ├─ status: PENDING/ACCEPTED/REJECTED
        │   ├─ friendsSince
        │   ├─ closeFriend
        │   └─ mutualFriendsCount
        │
        ├── DATING_WITH (hẹn hò) ──────────── Bidirectional
        │   ├─ status: SINGLE/IN_RELATIONSHIP/MARRIED...
        │   ├─ startedAt
        │   └─ anniversaryDate
        │
        ├── FOLLOWING (theo dõi) ──────────── One-way
        │   ├─ followedAt
        │   ├─ notificationsEnabled
        │   └─ showInFeed
        │
        ├── BLOCKED (chặn) ──────────── One-way
        │   ├─ blockedAt
        │   ├─ reason
        │   └─ isActive
        │
        ├── FAMILY_WITH (gia đình) ──────────── Bidirectional
        │   ├─ relationType: PARENT/CHILD/SIBLING...
        │   ├─ status: PENDING/CONFIRMED
        │   └─ isPublic
        │
        ├── COLLEAGUE_WITH (đồng nghiệp) ──────────── Bidirectional
        │   ├─ company
        │   ├─ position
        │   └─ isCurrentColleague
        │
        └── SCHOOLMATE_WITH (bạn học) ──────────── Bidirectional
            ├─ schoolName
            ├─ degree
            └─ classOf
```

### Chi tiết User → Group Relationships

```
      USER ──────────────────────► GROUP
        │
        ├── MEMBER_OF (thành viên)
        │   ├─ joinedAt
        │   ├─ status: PENDING/APPROVED/INVITED
        │   ├─ activityScore
        │   └─ notificationsEnabled
        │
        ├── ADMIN_OF (quản trị viên)
        │   ├─ appointedAt
        │   ├─ appointedBy
        │   ├─ permissions
        │   └─ isActive
        │
        ├── MODERATOR_OF (kiểm duyệt viên)
        │   ├─ appointedAt
        │   ├─ permissions
        │   ├─ moderationCount
        │   └─ isActive
        │
        ├── CREATOR_OF (người tạo)
        │   ├─ createdAt
        │   └─ canTransferOwnership
        │
        ├── BANNED_FROM (bị cấm)
        │   ├─ bannedAt
        │   ├─ bannedBy
        │   ├─ reason
        │   ├─ isPermanent
        │   └─ banExpiresAt
        │
        ├── INVITED_TO (được mời)
        │   ├─ invitedAt
        │   ├─ invitedBy
        │   ├─ status
        │   └─ expiresAt
        │
        └── REQUESTED_JOIN (yêu cầu tham gia)
            ├─ requestedAt
            ├─ status: PENDING/APPROVED/REJECTED
            ├─ reviewedBy
            └─ answers
```

### Chi tiết User → Page Relationships

```
      USER ──────────────────────► PAGE
        │
        ├── LIKE_PAGE (thích trang)
        │   ├─ likedAt
        │   ├─ notificationsEnabled
        │   └─ showInNewsfeed
        │
        ├── FOLLOW_PAGE (theo dõi trang)
        │   ├─ followedAt
        │   ├─ notificationsEnabled
        │   └─ notificationLevel: ALL/HIGHLIGHTS/OFF
        │
        └── ADMIN_PAGE (quản lý trang)
            ├─ role: OWNER/ADMIN/EDITOR/MODERATOR
            ├─ assignedAt
            ├─ assignedBy
            └─ permissions
```

## Quan hệ giữa các Node

```
┌──────────┐     FRIEND_WITH      ┌──────────┐
│          │◄───────────────────► │          │
│   USER   │                      │   USER   │
│          │     DATING_WITH      │          │
│          │◄───────────────────► │          │
│          │                      │          │
│          │     FOLLOWING        │          │
│          │─────────────────────►│          │
│          │                      │          │
│          │     BLOCKED          │          │
│          │─────────────────────►│          │
└─────┬────┘                      └──────────┘
      │
      │    MEMBER_OF
      │    ADMIN_OF
      │    MODERATOR_OF
      │    CREATOR_OF
      │    BANNED_FROM
      │
      ▼
┌──────────┐
│  GROUP   │
│          │
└──────────┘

      │
      │    LIKE_PAGE
      │    FOLLOW_PAGE
      │    ADMIN_PAGE
      │
      ▼
┌──────────┐
│   PAGE   │
│          │
└──────────┘
```

## Ví dụ về Graph Queries

### 1. Tìm bạn bè của bạn bè (Gợi ý kết bạn)
```cypher
MATCH (me:User {userId: $myId})-[:FRIEND_WITH]-(friend:User)-[:FRIEND_WITH]-(fof:User)
WHERE NOT (me)-[:FRIEND_WITH]-(fof) 
  AND NOT (me)-[:BLOCKED]-(fof)
  AND me <> fof
RETURN fof, COUNT(friend) as mutualFriendsCount
ORDER BY mutualFriendsCount DESC
LIMIT 10
```

### 2. Tìm nhóm được gợi ý dựa trên bạn bè
```cypher
MATCH (me:User {userId: $myId})-[:FRIEND_WITH]-(friend:User)-[:MEMBER_OF]->(group:Group)
WHERE NOT (me)-[:MEMBER_OF|BANNED_FROM]->(group)
RETURN group, COUNT(friend) as friendsInGroup
ORDER BY friendsInGroup DESC
LIMIT 10
```

### 3. Lấy tất cả mối quan hệ của một user
```cypher
MATCH (user:User {userId: $userId})-[r]-(other)
RETURN user, type(r) as relationshipType, r, other
```

### 4. Tìm người có cùng sở thích (thành viên nhiều nhóm chung)
```cypher
MATCH (me:User {userId: $myId})-[:MEMBER_OF]->(group:Group)<-[:MEMBER_OF]-(other:User)
WHERE me <> other 
  AND NOT (me)-[:FRIEND_WITH]-(other)
RETURN other, COUNT(group) as commonGroups
ORDER BY commonGroups DESC
LIMIT 10
```

### 5. Kiểm tra quyền của user trong nhóm
```cypher
MATCH (user:User {userId: $userId})-[r]->(group:Group {groupId: $groupId})
WHERE type(r) IN ['CREATOR_OF', 'ADMIN_OF', 'MODERATOR_OF', 'MEMBER_OF']
RETURN type(r) as role, r.permissions as permissions
```

## Các trạng thái mối quan hệ

### Người lạ (Stranger)
- Không có bất kỳ relationship nào giữa 2 user
- Query: `NOT EXISTS((user1)-[:FRIEND_WITH|BLOCKED|FOLLOWING]-(user2))`

### Bạn bè (Friend)
- Có relationship `FRIEND_WITH` với `status = 'ACCEPTED'`

### Đã chặn (Blocked)
- Có relationship `BLOCKED` với `isActive = true`

### Đang theo dõi (Following)
- Có relationship `FOLLOWING`

### Đã gửi yêu cầu kết bạn (Friend Request Sent)
- Có relationship `FRIEND_WITH` với `status = 'PENDING'` và `requestedBy = currentUserId`

### Nhận yêu cầu kết bạn (Friend Request Received)
- Có relationship `FRIEND_WITH` với `status = 'PENDING'` và `requestedBy != currentUserId`

## Performance Tips

1. **Indexes**: Tạo index cho các thuộc tính thường xuyên query
   ```cypher
   CREATE INDEX user_userId FOR (u:User) ON (u.userId)
   CREATE INDEX group_groupId FOR (g:Group) ON (g.groupId)
   CREATE INDEX page_pageId FOR (p:Page) ON (p.pageId)
   ```

2. **Constraints**: Đảm bảo tính duy nhất
   ```cypher
   CREATE CONSTRAINT user_userId_unique FOR (u:User) REQUIRE u.userId IS UNIQUE
   CREATE CONSTRAINT group_groupId_unique FOR (g:Group) REQUIRE g.groupId IS UNIQUE
   CREATE CONSTRAINT page_pageId_unique FOR (p:Page) REQUIRE p.pageId IS UNIQUE
   ```

3. **Pagination**: Sử dụng SKIP và LIMIT cho kết quả lớn
   ```cypher
   MATCH (user:User {userId: $userId})-[:FRIEND_WITH]-(friend)
   RETURN friend
   ORDER BY friend.username
   SKIP $offset
   LIMIT $pageSize
   ```

4. **Efficient Filtering**: Lọc sớm trong query
   ```cypher
   // Good
   MATCH (user:User {userId: $userId})-[r:FRIEND_WITH {status: 'ACCEPTED'}]-(friend)
   
   // Bad
   MATCH (user:User {userId: $userId})-[r:FRIEND_WITH]-(friend)
   WHERE r.status = 'ACCEPTED'
   ```
