# Danh sách đầy đủ các Neo4j Entities

## 📊 Tổng quan
Tổng cộng: **4 Nodes** và **21 Relationships** và **11 Enums**

---

## 🔵 NODES (Đỉnh)

### 1. User.java
- **Mô tả**: Đại diện cho người dùng trong hệ thống
- **Thuộc tính chính**: userId
- **Relationships**: Chứa tất cả các mối quan hệ với User khác, Group, Page và Location

### 2. Group.java
- **Mô tả**: Đại diện cho Nhóm/Group trên Facebook
- **Thuộc tính chính**: groupId, name, description, privacy, memberCount, category, isActive
- **Privacy levels**: PUBLIC, PRIVATE, SECRET

### 3. Page.java
- **Mô tả**: Đại diện cho Page/Fan Page
- **Thuộc tính chính**: pageId, name, category, isVerified, followerCount, likeCount
- **Category types**: Business, Brand, Artist, Public Figure, etc.

### 4. Location.java
- **Mô tả**: Đại diện cho Địa điểm (thành phố, trường học, công ty, nhà hàng, v.v.)
- **Thuộc tính chính**: locationId, name, type, address, city, country, latitude, longitude
- **Types**: CITY, SCHOOL, UNIVERSITY, COMPANY, RESTAURANT, CAFE, HOTEL, TOURIST_SPOT, etc.

---

## 🔗 RELATIONSHIPS (Mối quan hệ)

### A. User-to-User Relationships (7 loại)

#### 1. FriendRelationship.java
- **Type**: `FRIEND_WITH` (Bidirectional - Hai chiều)
- **Mô tả**: Mối quan hệ bạn bè
- **Thuộc tính**:
  - status: PENDING, ACCEPTED, REJECTED
  - friendsSince: Thời điểm trở thành bạn
  - requestedBy: Người gửi yêu cầu
  - closeFriend: Bạn thân
  - isBestFriend: Bạn thân nhất
  - mutualFriendsCount: Số bạn chung

#### 2. DatingRelationship.java
- **Type**: `DATING_WITH` (Bidirectional)
- **Mô tả**: Mối quan hệ hẹn hò/tình cảm
- **Thuộc tính**:
  - status: SINGLE, IN_RELATIONSHIP, ENGAGED, MARRIED, COMPLICATED, SEPARATED, DIVORCED, WIDOWED
  - startedAt: Thời điểm bắt đầu
  - isPublic: Có công khai không
  - anniversaryDate: Ngày kỷ niệm

#### 3. BlockRelationship.java
- **Type**: `BLOCKED` (One-way - Một chiều)
- **Mô tả**: User chặn user khác
- **Thuộc tính**:
  - blockedAt: Thời điểm chặn
  - reason: Lý do chặn
  - isActive: Trạng thái chặn

#### 4. FollowRelationship.java
- **Type**: `FOLLOWING` (One-way)
- **Mô tả**: User theo dõi user khác (không cần chấp nhận)
- **Thuộc tính**:
  - followedAt: Thời điểm theo dõi
  - notificationsEnabled: Bật thông báo
  - showInFeed: Hiển thị trong newsfeed

#### 5. FamilyRelationship.java
- **Type**: `FAMILY_WITH` (Bidirectional)
- **Mô tả**: Mối quan hệ gia đình
- **Thuộc tính**:
  - relationType: PARENT, CHILD, SIBLING, SPOUSE, GRANDPARENT, UNCLE, AUNT, COUSIN, etc.
  - status: PENDING, CONFIRMED, REJECTED
  - isPublic: Có công khai không
  - confirmedBy: Người xác nhận

#### 6. ColleagueRelationship.java
- **Type**: `COLLEAGUE_WITH` (Bidirectional)
- **Mô tả**: Mối quan hệ đồng nghiệp
- **Thuộc tính**:
  - company: Tên công ty
  - position: Vị trí
  - department: Phòng ban
  - workedTogetherSince: Thời điểm bắt đầu làm việc cùng
  - isCurrentColleague: Đang làm việc cùng

#### 7. SchoolmateRelationship.java
- **Type**: `SCHOOLMATE_WITH` (Bidirectional)
- **Mô tả**: Mối quan hệ bạn học
- **Thuộc tính**:
  - schoolName: Tên trường
  - degree: Bậc học
  - major: Chuyên ngành
  - classOf: Khóa
  - isCurrentSchoolmate: Đang học cùng

---

### B. User-to-Group Relationships (7 loại)

#### 8. GroupMemberRelationship.java
- **Type**: `MEMBER_OF`
- **Mô tả**: User là thành viên của Group
- **Thuộc tính**:
  - joinedAt: Thời điểm tham gia
  - status: PENDING, APPROVED, INVITED
  - invitedBy: Người mời
  - notificationsEnabled: Bật thông báo
  - activityScore: Điểm hoạt động
  - lastActive: Lần cuối hoạt động

#### 9. GroupAdminRelationship.java
- **Type**: `ADMIN_OF`
- **Mô tả**: User là admin của Group
- **Thuộc tính**:
  - appointedAt: Thời điểm bổ nhiệm
  - appointedBy: Người bổ nhiệm
  - permissions: Danh sách quyền
  - isActive: Trạng thái hoạt động

#### 10. GroupModeratorRelationship.java
- **Type**: `MODERATOR_OF`
- **Mô tả**: User là moderator của Group
- **Thuộc tính**:
  - appointedAt: Thời điểm bổ nhiệm
  - appointedBy: Người bổ nhiệm
  - permissions: Quyền kiểm duyệt
  - moderationCount: Số lượng đã kiểm duyệt
  - isActive: Trạng thái

#### 11. GroupCreatorRelationship.java
- **Type**: `CREATOR_OF`
- **Mô tả**: User là người tạo Group (duy nhất)
- **Thuộc tính**:
  - createdAt: Thời điểm tạo
  - canTransferOwnership: Có thể chuyển quyền sở hữu

#### 12. GroupBanRelationship.java
- **Type**: `BANNED_FROM`
- **Mô tả**: User bị cấm khỏi Group
- **Thuộc tính**:
  - bannedAt: Thời điểm bị ban
  - bannedBy: Người ban
  - reason: Lý do
  - isPermanent: Ban vĩnh viễn
  - banExpiresAt: Thời điểm hết hạn
  - canAppeal: Có thể khiếu nại

#### 13. GroupInvitationRelationship.java
- **Type**: `INVITED_TO`
- **Mô tả**: User được mời vào Group
- **Thuộc tính**:
  - invitedAt: Thời điểm được mời
  - invitedBy: Người mời
  - status: PENDING, ACCEPTED, DECLINED, EXPIRED
  - expiresAt: Thời điểm hết hạn
  - invitationMessage: Lời nhắn

#### 14. GroupJoinRequestRelationship.java
- **Type**: `REQUESTED_JOIN`
- **Mô tả**: User yêu cầu tham gia Group (private/secret)
- **Thuộc tính**:
  - requestedAt: Thời điểm yêu cầu
  - status: PENDING, APPROVED, REJECTED
  - reviewedBy: Người xét duyệt
  - reviewedAt: Thời điểm xét duyệt
  - rejectionReason: Lý do từ chối
  - answers: Câu trả lời cho câu hỏi nhóm

---

### C. User-to-Page Relationships (3 loại)

#### 15. PageLikeRelationship.java
- **Type**: `LIKE_PAGE`
- **Mô tả**: User thích Page
- **Thuộc tính**:
  - likedAt: Thời điểm thích
  - notificationsEnabled: Bật thông báo
  - showInNewsfeed: Hiển thị trong newsfeed

#### 16. PageFollowRelationship.java
- **Type**: `FOLLOW_PAGE`
- **Mô tả**: User theo dõi Page
- **Thuộc tính**:
  - followedAt: Thời điểm theo dõi
  - notificationsEnabled: Bật thông báo
  - notificationLevel: ALL, HIGHLIGHTS, OFF

#### 17. PageAdminRelationship.java
- **Type**: `ADMIN_PAGE`
- **Mô tả**: User là admin/quản lý Page
- **Thuộc tính**:
  - role: OWNER, ADMIN, EDITOR, MODERATOR, ADVERTISER, ANALYST
  - assignedAt: Thời điểm được gán
  - assignedBy: Người gán
  - permissions: Danh sách quyền
  - isActive: Trạng thái

---

### D. User-to-Location Relationships (4 loại)

#### 18. LivesInRelationship.java
- **Type**: `LIVES_IN`
- **Mô tả**: User đang sống hoặc đã từng sống tại địa điểm
- **Thuộc tính**:
  - type: CURRENT, HOMETOWN, PREVIOUS
  - startedAt: Thời điểm bắt đầu sống
  - endedAt: Thời điểm kết thúc
  - isCurrent: Có đang sống không
  - isHometown: Có phải quê quán không
  - isPublic: Có công khai không

#### 19. StudiedAtRelationship.java
- **Type**: `STUDIED_AT`
- **Mô tả**: User đã học hoặc đang học tại trường/đại học
- **Thuộc tính**:
  - schoolName: Tên trường
  - degree: Bậc học (ELEMENTARY, HIGH_SCHOOL, UNIVERSITY, etc.)
  - major: Chuyên ngành
  - startYear/endYear: Năm bắt đầu/kết thúc
  - graduated: Đã tốt nghiệp chưa
  - isCurrentStudent: Có đang học không
  - activities: Hoạt động, câu lạc bộ

#### 20. WorksAtRelationship.java
- **Type**: `WORKS_AT`
- **Mô tả**: User đang làm hoặc đã từng làm việc tại công ty
- **Thuộc tính**:
  - companyName: Tên công ty
  - position: Vị trí công việc
  - department: Phòng ban
  - employmentType: FULL_TIME, PART_TIME, CONTRACT, FREELANCE, INTERNSHIP
  - startDate/endDate: Ngày bắt đầu/kết thúc
  - isCurrent: Có đang làm việc không
  - description: Mô tả công việc

#### 21. VisitedRelationship.java
- **Type**: `VISITED`
- **Mô tả**: User đã check-in hoặc ghé thăm địa điểm
- **Thuộc tính**:
  - visitedAt: Thời điểm ghé thăm
  - visitCount: Số lần ghé thăm
  - rating: Đánh giá (1-5 sao)
  - review: Nhận xét
  - photos: Danh sách ID ảnh
  - taggedUsers: Người được tag cùng
  - permissions: Danh sách quyền
  - isActive: Trạng thái

---

## 📋 ENUMS (Liệt kê)

### 1. FriendStatus.java
```
PENDING, ACCEPTED, REJECTED
```

### 2. RelationshipStatus.java
```
SINGLE, IN_RELATIONSHIP, ENGAGED, MARRIED, 
COMPLICATED, SEPARATED, DIVORCED, WIDOWED
```

### 3. GroupMemberStatus.java
```
PENDING, APPROVED, INVITED
```

### 4. GroupPrivacy.java
```
PUBLIC, PRIVATE, SECRET
```

### 5. GroupPermission.java
```
Admin: MANAGE_MEMBERS, APPROVE_POSTS, DELETE_POSTS, EDIT_GROUP_INFO, 
       MANAGE_ADMINS, MANAGE_MODERATORS, DELETE_GROUP

Moderator: DELETE_COMMENTS, WARN_MEMBERS, BAN_MEMBERS, 
           APPROVE_MEMBER_POSTS, PIN_POSTS

Member: POST_IN_GROUP, COMMENT, INVITE_MEMBERS
```

### 6. FamilyRelationType.java
```
PARENT, CHILD, SIBLING, SPOUSE, GRANDPARENT, GRANDCHILD, 
UNCLE, AUNT, COUSIN, NEPHEW, NIECE, PARENT_IN_LAW, 
SIBLING_IN_LAW, STEP_PARENT, STEP_CHILD, STEP_SIBLING, OTHER
```

### 7. ConfirmationStatus.java
```
PENDING, CONFIRMED, REJECTED
```

### 8. LocationType.java
```
CITY, COUNTRY, SCHOOL, UNIVERSITY, COLLEGE, COMPANY, WORKPLACE,
RESTAURANT, CAFE, HOTEL, TOURIST_SPOT, SHOPPING_MALL, HOSPITAL,
PARK, GYM, LIBRARY, MUSEUM, THEATER, STADIUM, AIRPORT, OTHER
```

### 9. EducationLevel.java
```
ELEMENTARY, MIDDLE_SCHOOL, HIGH_SCHOOL, VOCATIONAL, COLLEGE,
UNIVERSITY, BACHELOR, MASTER, DOCTORATE, POSTDOC, OTHER
```

### 10. EmploymentType.java
```
FULL_TIME, PART_TIME, CONTRACT, TEMPORARY, FREELANCE,
INTERNSHIP, VOLUNTEER, SEASONAL, SELF_EMPLOYED, OTHER
```

### 11. ResidenceType.java
```
CURRENT, HOMETOWN, PREVIOUS, TEMPORARY
```

---

## 📁 Cấu trúc thư mục

```
src/main/java/com/tam/relationship/entity/
├── User.java                          # Node: User
├── Group.java                         # Node: Group
├── Page.java                          # Node: Page
├── Location.java                      # Node: Location
│
├── FriendRelationship.java            # User-User: Bạn bè
├── DatingRelationship.java            # User-User: Hẹn hò
├── BlockRelationship.java             # User-User: Chặn
├── FollowRelationship.java            # User-User: Theo dõi
├── FamilyRelationship.java            # User-User: Gia đình
├── SchoolmateRelationship.java        # User-User: Bạn học
│
├── GroupMemberRelationship.java       # User-Group: Thành viên
├── GroupAdminRelationship.java        # User-Group: Quản trị viên
├── GroupModeratorRelationship.java    # User-Group: Kiểm duyệt viên
├── GroupCreatorRelationship.java      # User-Group: Người tạo
├── GroupBanRelationship.java          # User-Group: Bị cấm
├── GroupInvitationRelationship.java   # User-Group: Được mời
├── GroupJoinRequestRelationship.java  # User-Group: Yêu cầu tham gia
│
├── PageLikeRelationship.java          # User-Page: Thích
├── PageFollowRelationship.java        # User-Page: Theo dõi
├── PageAdminRelationship.java         # User-Page: Quản lý
│
├── LivesInRelationship.java           # User-Location: Sống tại
├── StudiedAtRelationship.java         # User-Location: Học tại
├── WorksAtRelationship.java           # User-Location: Làm việc tại
├── VisitedRelationship.java           # User-Location: Đã ghé thăm
│
└── enums/
    ├── FriendStatus.java
    ├── RelationshipStatus.java
    ├── GroupMemberStatus.java
    ├── GroupPrivacy.java
    ├── GroupPermission.java
    ├── FamilyRelationType.java
    ├── ConfirmationStatus.java
    ├── LocationType.java
    ├── EducationLevel.java
    ├── EmploymentType.java
    └── ResidenceType.java
```
├── Group.java                         # Node: Group
├── Page.java                          # Node: Page
│
├── FriendRelationship.java            # User-User: Bạn bè
├── DatingRelationship.java            # User-User: Hẹn hò
├── BlockRelationship.java             # User-User: Chặn
├── FollowRelationship.java            # User-User: Theo dõi
├── FamilyRelationship.java            # User-User: Gia đình
├── ColleagueRelationship.java         # User-User: Đồng nghiệp
├── SchoolmateRelationship.java        # User-User: Bạn học
│
├── GroupMemberRelationship.java       # User-Group: Thành viên
├── GroupAdminRelationship.java        # User-Group: Quản trị viên
├── GroupModeratorRelationship.java    # User-Group: Kiểm duyệt viên
├── GroupCreatorRelationship.java      # User-Group: Người tạo
├── GroupBanRelationship.java          # User-Group: Bị cấm
├── GroupInvitationRelationship.java   # User-Group: Được mời
├── GroupJoinRequestRelationship.java  # User-Group: Yêu cầu tham gia
│
├── PageLikeRelationship.java          # User-Page: Thích
├── PageFollowRelationship.java        # User-Page: Theo dõi
├── PageAdminRelationship.java         # User-Page: Quản lý
│
└── enums/
    ├── FriendStatus.java
    ├── RelationshipStatus.java
    ├── GroupMemberStatus.java
    ├── GroupPrivacy.java
    ├── GroupPermission.java
    ├── FamilyRelationType.java
    └── ConfirmationStatus.java
```

---

## 🎯 Use Cases chính

### 1. Social Graph (Đồ thị xã hội)
- Tìm bạn bè của bạn bè (Friend suggestions)
- Tính toán độ gần (Social distance)
- Phát hiện cộng đồng (Community detection)
- Bạn chung (Mutual friends)

### 2. Group Management (Quản lý nhóm)
- Phân quyền thành viên
- Kiểm soát truy cập
- Theo dõi hoạt động
- Gợi ý nhóm dựa trên bạn bè

### 3. Page Engagement (Tương tác với Page)
- Theo dõi followers
- Quản lý admin/roles
- Phân tích engagement
- Gợi ý Page dựa trên sở thích

### 4. Privacy & Security (Bảo mật)
- Kiểm tra block status
- Phân quyền xem nội dung
- Privacy settings
- Content filtering

### 5. Recommendations (Gợi ý)
- Friend suggestions
- Group suggestions
- Page suggestions
- Content recommendations

---

## 🚀 Next Steps

### 1. Tạo Repositories
- UserRepository extends Neo4jRepository
- GroupRepository
- PageRepository

### 2. Tạo Services
- UserRelationshipService: Xử lý các mối quan hệ User-User
- GroupService: Xử lý các mối quan hệ User-Group
- PageService: Xử lý các mối quan hệ User-Page

### 3. Tạo DTOs
- Request DTOs cho các API endpoints
- Response DTOs cho các relationships

### 4. Tạo Controllers
- UserRelationshipController: APIs cho friend, follow, block, etc.
- GroupController: APIs cho group membership, admin, etc.
- PageController: APIs cho page like, follow, admin, etc.

### 5. Write Cypher Queries
- Friend suggestions
- Mutual friends
- Group recommendations
- Privacy checks

---

## 📚 Tài liệu tham khảo

- **RELATIONSHIP_ENTITIES_README.md**: Chi tiết về từng entity và use cases
- **GRAPH_STRUCTURE_DIAGRAM.md**: Sơ đồ cấu trúc và ví dụ Cypher queries

---

## ✅ Checklist

- [x] Tạo 3 Node entities (User, Group, Page)
- [x] Tạo 7 User-User relationships
- [x] Tạo 7 User-Group relationships
- [x] Tạo 3 User-Page relationships
- [x] Tạo 7 Enums
- [x] Viết documentation
- [ ] Tạo Repositories
- [ ] Tạo Services
- [ ] Tạo Controllers
- [ ] Tạo DTOs
- [ ] Viết Unit Tests
- [ ] Tạo Integration Tests
- [ ] Setup Neo4j configuration
- [ ] Create indexes và constraints
- [ ] Performance optimization

---

**Created by**: AI Assistant
**Date**: 2025
**Version**: 1.0
