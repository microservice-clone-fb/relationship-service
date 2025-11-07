# Neo4j Relationship Entities Documentation

## Tổng quan

Hệ thống Neo4j này được thiết kế để quản lý các mối quan hệ phức tạp trên mạng xã hội Facebook, bao gồm quan hệ giữa người dùng với nhau và giữa người dùng với nhóm.

## Cấu trúc Graph Database

### Nodes (Đỉnh)
1. **User** - Đại diện cho người dùng
2. **Group** - Đại diện cho nhóm/cộng đồng

### Relationships (Cạnh)

#### A. Quan hệ User-User

##### 1. **FRIEND_WITH** (Bạn bè)
- **Entity**: `FriendRelationship`
- **Mô tả**: Mối quan hệ bạn bè giữa 2 người dùng (hai chiều)
- **Thuộc tính**:
  - `status`: PENDING, ACCEPTED, REJECTED
  - `friendsSince`: Thời điểm trở thành bạn bè
  - `requestedBy`: Người gửi yêu cầu kết bạn
  - `closeFriend`: Có phải bạn thân không
  - `isBestFriend`: Có phải bạn thân nhất không
  - `mutualFriendsCount`: Số bạn chung

##### 2. **DATING_WITH** (Hẹn hò)
- **Entity**: `DatingRelationship`
- **Mô tả**: Mối quan hệ tình cảm giữa 2 người dùng
- **Thuộc tính**:
  - `status`: SINGLE, IN_RELATIONSHIP, ENGAGED, MARRIED, COMPLICATED, SEPARATED, DIVORCED, WIDOWED
  - `startedAt`: Thời điểm bắt đầu mối quan hệ
  - `isPublic`: Có công khai không
  - `anniversaryDate`: Ngày kỷ niệm

##### 3. **BLOCKED** (Chặn)
- **Entity**: `BlockRelationship`
- **Mô tả**: User chặn một user khác (một chiều)
- **Thuộc tính**:
  - `blockedAt`: Thời điểm chặn
  - `reason`: Lý do chặn
  - `isActive`: Trạng thái chặn

##### 4. **FOLLOWING** (Theo dõi)
- **Entity**: `FollowRelationship`
- **Mô tả**: User theo dõi một user khác (một chiều, không cần chấp nhận)
- **Thuộc tính**:
  - `followedAt`: Thời điểm bắt đầu theo dõi
  - `notificationsEnabled`: Có bật thông báo không
  - `showInFeed`: Hiển thị trong newsfeed không

##### 5. **FAMILY_WITH** (Gia đình)
- **Entity**: `FamilyRelationship`
- **Mô tả**: Mối quan hệ huyết thống hoặc hôn nhân
- **Thuộc tính**:
  - `relationType`: PARENT, CHILD, SIBLING, SPOUSE, GRANDPARENT, UNCLE, AUNT, COUSIN, etc.
  - `isPublic`: Có công khai không
  - `status`: PENDING, CONFIRMED, REJECTED

##### 6. **COLLEAGUE_WITH** (Đồng nghiệp)
- **Entity**: `ColleagueRelationship`
- **Mô tả**: Mối quan hệ công việc
- **Thuộc tính**:
  - `company`: Tên công ty
  - `position`: Vị trí
  - `department`: Phòng ban
  - `workedTogetherSince`: Thời điểm bắt đầu làm việc cùng nhau
  - `isCurrentColleague`: Có đang làm việc cùng không

#### B. Quan hệ User-Group

##### 1. **MEMBER_OF** (Thành viên)
- **Entity**: `GroupMemberRelationship`
- **Mô tả**: User là thành viên của Group
- **Thuộc tính**:
  - `joinedAt`: Thời điểm tham gia
  - `status`: PENDING, APPROVED, INVITED
  - `invitedBy`: Người mời (nếu có)
  - `notificationsEnabled`: Có bật thông báo không
  - `activityScore`: Điểm hoạt động
  - `lastActive`: Lần cuối hoạt động

##### 2. **ADMIN_OF** (Quản trị viên)
- **Entity**: `GroupAdminRelationship`
- **Mô tả**: User là admin của Group với quyền quản lý cao
- **Thuộc tính**:
  - `appointedAt`: Thời điểm được bổ nhiệm
  - `appointedBy`: Người bổ nhiệm
  - `permissions`: Danh sách quyền (MANAGE_MEMBERS, APPROVE_POSTS, DELETE_POSTS, EDIT_GROUP_INFO, etc.)
  - `isActive`: Trạng thái hoạt động

##### 3. **MODERATOR_OF** (Người kiểm duyệt)
- **Entity**: `GroupModeratorRelationship`
- **Mô tả**: User là moderator của Group với quyền kiểm duyệt
- **Thuộc tính**:
  - `appointedAt`: Thời điểm được bổ nhiệm
  - `appointedBy`: Người bổ nhiệm
  - `permissions`: Quyền kiểm duyệt (APPROVE_POSTS, DELETE_COMMENTS, WARN_MEMBERS, etc.)
  - `moderationCount`: Số lượng đã kiểm duyệt
  - `isActive`: Trạng thái

##### 4. **CREATOR_OF** (Người tạo nhóm)
- **Entity**: `GroupCreatorRelationship`
- **Mô tả**: User là người tạo ra Group (duy nhất)
- **Thuộc tính**:
  - `createdAt`: Thời điểm tạo
  - `canTransferOwnership`: Có thể chuyển quyền sở hữu không

##### 5. **BANNED_FROM** (Bị cấm)
- **Entity**: `GroupBanRelationship`
- **Mô tả**: User bị cấm khỏi Group
- **Thuộc tính**:
  - `bannedAt`: Thời điểm bị ban
  - `bannedBy`: Người ban
  - `reason`: Lý do
  - `isPermanent`: Ban vĩnh viễn hay tạm thời
  - `banExpiresAt`: Thời điểm hết hạn (nếu tạm thời)
  - `canAppeal`: Có thể khiếu nại không

## Các Entity Enums

### FriendStatus
```java
PENDING, ACCEPTED, REJECTED
```

### RelationshipStatus
```java
SINGLE, IN_RELATIONSHIP, ENGAGED, MARRIED, COMPLICATED, SEPARATED, DIVORCED, WIDOWED
```

### GroupMemberStatus
```java
PENDING, APPROVED, INVITED
```

### GroupPrivacy
```java
PUBLIC, PRIVATE, SECRET
```

### GroupPermission
```java
// Admin
MANAGE_MEMBERS, APPROVE_POSTS, DELETE_POSTS, EDIT_GROUP_INFO, MANAGE_ADMINS, MANAGE_MODERATORS, DELETE_GROUP

// Moderator
DELETE_COMMENTS, WARN_MEMBERS, BAN_MEMBERS, APPROVE_MEMBER_POSTS, PIN_POSTS

// Member
POST_IN_GROUP, COMMENT, INVITE_MEMBERS
```

### FamilyRelationType
```java
PARENT, CHILD, SIBLING, SPOUSE, GRANDPARENT, GRANDCHILD, UNCLE, AUNT, COUSIN, 
NEPHEW, NIECE, PARENT_IN_LAW, SIBLING_IN_LAW, STEP_PARENT, STEP_CHILD, STEP_SIBLING, OTHER
```

## Các gợi ý bổ sung

### 1. Mối quan hệ User-User bổ sung có thể thêm:
- **SCHOOLMATE_WITH** (Bạn học): Quan hệ học cùng trường
- **MENTOR_OF** (Người cố vấn): Quan hệ mentor-mentee
- **NEIGHBOR_WITH** (Hàng xóm): Quan hệ láng giềng
- **RECOMMENDED_TO** (Gợi ý): Hệ thống gợi ý kết bạn

### 2. Mối quan hệ User-Group bổ sung có thể thêm:
- **INVITED_TO** (Được mời): User được mời vào nhóm nhưng chưa tham gia
- **REQUESTED_JOIN** (Yêu cầu tham gia): User yêu cầu tham gia nhóm private
- **LEFT_GROUP** (Đã rời): User đã rời khỏi nhóm (lưu lịch sử)
- **REMOVED_FROM** (Bị xóa): User bị xóa khỏi nhóm bởi admin

### 3. Các Node bổ sung có thể thêm:
- **Page** (Trang): Đại diện cho fan page
- **Event** (Sự kiện): Đại diện cho event
- **Interest** (Sở thích): Đại diện cho sở thích chung

### 4. Các thuộc tính nên có trên User Node:
- Privacy settings (cài đặt riêng tư)
- Verification status (trạng thái xác minh)
- Account status (trạng thái tài khoản)

## Use Cases phổ biến

### 1. Tìm bạn chung
```cypher
MATCH (user1:User {userId: $userId1})-[:FRIEND_WITH]-(mutual:User)-[:FRIEND_WITH]-(user2:User {userId: $userId2})
RETURN mutual
```

### 2. Tìm người lạ (không có quan hệ)
```cypher
MATCH (user1:User {userId: $userId1})
MATCH (user2:User)
WHERE user1 <> user2
  AND NOT (user1)-[:FRIEND_WITH|BLOCKED|FOLLOWING]-(user2)
RETURN user2
```

### 3. Lấy tất cả admin của một nhóm
```cypher
MATCH (user:User)-[r:ADMIN_OF]->(group:Group {groupId: $groupId})
WHERE r.isActive = true
RETURN user, r
```

### 4. Kiểm tra user có bị chặn không
```cypher
MATCH (user1:User {userId: $userId1})-[r:BLOCKED]->(user2:User {userId: $userId2})
WHERE r.isActive = true
RETURN r IS NOT NULL as isBlocked
```

### 5. Lấy danh sách nhóm user tham gia
```cypher
MATCH (user:User {userId: $userId})-[r:MEMBER_OF|ADMIN_OF|MODERATOR_OF|CREATOR_OF]->(group:Group)
RETURN group, type(r) as role, r
```

## Best Practices

1. **Indexing**: Tạo index cho `userId` và `groupId` để tăng performance
2. **Timestamp**: Luôn lưu timestamp cho mọi mối quan hệ
3. **Status Fields**: Sử dụng status để soft delete thay vì xóa cứng
4. **Bidirectional**: Quan hệ bạn bè nên là hai chiều
5. **Validation**: Validate trước khi tạo quan hệ (ví dụ: không thể kết bạn với chính mình)

## Cấu hình Neo4j

Đảm bảo `application.yaml` có cấu hình:
```yaml
spring:
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: your-password
```

## Dependencies

Đảm bảo `pom.xml` đã có:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-neo4j</artifactId>
</dependency>
```
