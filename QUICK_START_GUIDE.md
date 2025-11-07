# 🎯 Quick Start Guide - Neo4j Relationship Service

## 📚 Tổng quan

Đây là hệ thống quản lý mối quan hệ cho ứng dụng clone Facebook sử dụng Neo4j Graph Database. Hệ thống này cho phép quản lý các mối quan hệ phức tạp giữa người dùng, nhóm và trang.

## 🏗️ Kiến trúc

```
┌─────────────────────────────────────────────────────────┐
│                    Application Layer                    │
│              (Controllers + Services + DTOs)             │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                 Spring Data Neo4j                       │
│           (Repositories + Entity Mapping)               │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                   Neo4j Database                        │
│             (Nodes + Relationships)                     │
└─────────────────────────────────────────────────────────┘
```

## 📦 Đã tạo sẵn

### ✅ Entities (3 Nodes + 18 Relationships)

#### Nodes
- ✅ `User.java` - Người dùng
- ✅ `Group.java` - Nhóm
- ✅ `Page.java` - Trang

#### User-User Relationships
- ✅ `FriendRelationship.java` - Bạn bè
- ✅ `DatingRelationship.java` - Hẹn hò
- ✅ `BlockRelationship.java` - Chặn
- ✅ `FollowRelationship.java` - Theo dõi
- ✅ `FamilyRelationship.java` - Gia đình
- ✅ `ColleagueRelationship.java` - Đồng nghiệp
- ✅ `SchoolmateRelationship.java` - Bạn học

#### User-Group Relationships
- ✅ `GroupMemberRelationship.java` - Thành viên
- ✅ `GroupAdminRelationship.java` - Quản trị viên
- ✅ `GroupModeratorRelationship.java` - Kiểm duyệt viên
- ✅ `GroupCreatorRelationship.java` - Người tạo
- ✅ `GroupBanRelationship.java` - Bị cấm
- ✅ `GroupInvitationRelationship.java` - Được mời
- ✅ `GroupJoinRequestRelationship.java` - Yêu cầu tham gia

#### User-Page Relationships
- ✅ `PageLikeRelationship.java` - Thích
- ✅ `PageFollowRelationship.java` - Theo dõi
- ✅ `PageAdminRelationship.java` - Quản lý

#### Enums
- ✅ 7 Enums trong thư mục `enums/`

### 📖 Documentation
- ✅ `ENTITIES_SUMMARY.md` - Tổng hợp danh sách entities
- ✅ `RELATIONSHIP_ENTITIES_README.md` - Chi tiết từng entity
- ✅ `GRAPH_STRUCTURE_DIAGRAM.md` - Sơ đồ và Cypher queries
- ✅ `QUICK_START_GUIDE.md` - Hướng dẫn này

## 🚀 Các bước tiếp theo

### Bước 1: Cấu hình Neo4j

#### 1.1. Cài đặt Neo4j
```bash
# Sử dụng Docker (khuyến nghị)
docker run -d \
  --name neo4j \
  -p 7474:7474 -p 7687:7687 \
  -e NEO4J_AUTH=neo4j/your-password \
  neo4j:latest
```

#### 1.2. Cập nhật application.yaml
```yaml
spring:
  neo4j:
    uri: bolt://localhost:7687
    authentication:
      username: neo4j
      password: your-password
  data:
    neo4j:
      database: neo4j
```

### Bước 2: Tạo Repositories

Tạo file trong `repository/` package:

```java
// UserRepository.java
@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
    Optional<User> findByUserId(String userId);
    
    @Query("MATCH (u:User {userId: $userId})-[r:FRIEND_WITH {status: 'ACCEPTED'}]-(friend:User) RETURN friend")
    List<User> findFriends(@Param("userId") String userId);
    
    @Query("MATCH (u1:User {userId: $userId1})-[:FRIEND_WITH]-(mutual:User)-[:FRIEND_WITH]-(u2:User {userId: $userId2}) RETURN mutual")
    List<User> findMutualFriends(@Param("userId1") String userId1, @Param("userId2") String userId2);
    
    @Query("MATCH (u:User {userId: $userId})-[r:BLOCKED {isActive: true}]->(blocked:User) RETURN blocked")
    List<User> findBlockedUsers(@Param("userId") String userId);
}

// GroupRepository.java
@Repository
public interface GroupRepository extends Neo4jRepository<Group, String> {
    Optional<Group> findByGroupId(String groupId);
    
    @Query("MATCH (u:User {userId: $userId})-[:MEMBER_OF]->(g:Group) RETURN g")
    List<Group> findGroupsByMemberId(@Param("userId") String userId);
    
    @Query("MATCH (u:User {userId: $userId})-[:ADMIN_OF]->(g:Group) RETURN g")
    List<Group> findGroupsAdminedByUser(@Param("userId") String userId);
}

// PageRepository.java
@Repository
public interface PageRepository extends Neo4jRepository<Page, String> {
    Optional<Page> findByPageId(String pageId);
    
    @Query("MATCH (u:User {userId: $userId})-[:LIKE_PAGE]->(p:Page) RETURN p")
    List<Page> findLikedPagesByUser(@Param("userId") String userId);
    
    @Query("MATCH (u:User {userId: $userId})-[:ADMIN_PAGE]->(p:Page) RETURN p")
    List<Page> findPagesAdminedByUser(@Param("userId") String userId);
}
```

### Bước 3: Tạo DTOs

Tạo file trong `dto/request/` và `dto/response/`:

```java
// dto/request/SendFriendRequestDto.java
@Data
public class SendFriendRequestDto {
    @NotBlank
    private String targetUserId;
}

// dto/response/FriendDto.java
@Data
@Builder
public class FriendDto {
    private String userId;
    private String username;
    private String email;
    private LocalDateTime friendsSince;
    private Boolean closeFriend;
    private Integer mutualFriendsCount;
}

// dto/request/JoinGroupDto.java
@Data
public class JoinGroupDto {
    @NotBlank
    private String groupId;
    private String invitedBy; // Optional
    private String answers; // JSON for group questions
}

// dto/response/GroupMemberDto.java
@Data
@Builder
public class GroupMemberDto {
    private String userId;
    private String username;
    private String role; // MEMBER, ADMIN, MODERATOR, CREATOR
    private LocalDateTime joinedAt;
    private Integer activityScore;
}
```

### Bước 4: Tạo Services

Tạo file trong `service/` package:

```java
// UserRelationshipService.java
@Service
@RequiredArgsConstructor
public class UserRelationshipService {
    private final UserRepository userRepository;
    
    // Friend operations
    public void sendFriendRequest(String fromUserId, String toUserId) {
        // Implementation
    }
    
    public void acceptFriendRequest(String userId, String requesterId) {
        // Implementation
    }
    
    public List<FriendDto> getFriends(String userId) {
        // Implementation
    }
    
    public List<User> getMutualFriends(String userId1, String userId2) {
        // Implementation
    }
    
    // Follow operations
    public void followUser(String followerId, String followedUserId) {
        // Implementation
    }
    
    public void unfollowUser(String followerId, String followedUserId) {
        // Implementation
    }
    
    // Block operations
    public void blockUser(String userId, String blockedUserId, String reason) {
        // Implementation
    }
    
    public void unblockUser(String userId, String blockedUserId) {
        // Implementation
    }
    
    public boolean isBlocked(String userId, String targetUserId) {
        // Implementation
    }
}

// GroupService.java
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    
    public void joinGroup(String userId, String groupId) {
        // Implementation
    }
    
    public void leaveGroup(String userId, String groupId) {
        // Implementation
    }
    
    public void appointAdmin(String adminId, String userId, String groupId) {
        // Implementation
    }
    
    public void banMember(String adminId, String userId, String groupId, String reason) {
        // Implementation
    }
    
    public List<GroupMemberDto> getGroupMembers(String groupId) {
        // Implementation
    }
}

// PageService.java
@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;
    
    public void likePage(String userId, String pageId) {
        // Implementation
    }
    
    public void unlikePage(String userId, String pageId) {
        // Implementation
    }
    
    public void followPage(String userId, String pageId) {
        // Implementation
    }
}
```

### Bước 5: Tạo Controllers

Tạo file trong `controller/` package:

```java
// UserRelationshipController.java
@RestController
@RequestMapping("/api/v1/relationships")
@RequiredArgsConstructor
public class UserRelationshipController {
    private final UserRelationshipService relationshipService;
    
    @PostMapping("/friends/request")
    public ApiResponse<Void> sendFriendRequest(@RequestBody SendFriendRequestDto dto) {
        // Implementation
    }
    
    @PostMapping("/friends/accept/{requesterId}")
    public ApiResponse<Void> acceptFriendRequest(@PathVariable String requesterId) {
        // Implementation
    }
    
    @GetMapping("/friends")
    public ApiResponse<List<FriendDto>> getFriends() {
        // Implementation
    }
    
    @PostMapping("/follow/{userId}")
    public ApiResponse<Void> followUser(@PathVariable String userId) {
        // Implementation
    }
    
    @PostMapping("/block/{userId}")
    public ApiResponse<Void> blockUser(@PathVariable String userId, @RequestBody BlockUserDto dto) {
        // Implementation
    }
}

// GroupController.java
@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    
    @PostMapping("/{groupId}/join")
    public ApiResponse<Void> joinGroup(@PathVariable String groupId, @RequestBody JoinGroupDto dto) {
        // Implementation
    }
    
    @DeleteMapping("/{groupId}/leave")
    public ApiResponse<Void> leaveGroup(@PathVariable String groupId) {
        // Implementation
    }
    
    @GetMapping("/{groupId}/members")
    public ApiResponse<List<GroupMemberDto>> getMembers(@PathVariable String groupId) {
        // Implementation
    }
}
```

### Bước 6: Tạo Indexes và Constraints

Chạy các Cypher queries sau trong Neo4j Browser (http://localhost:7474):

```cypher
// Create Indexes
CREATE INDEX user_userId IF NOT EXISTS FOR (u:User) ON (u.userId);
CREATE INDEX group_groupId IF NOT EXISTS FOR (g:Group) ON (g.groupId);
CREATE INDEX page_pageId IF NOT EXISTS FOR (p:Page) ON (p.pageId);

// Create Constraints
CREATE CONSTRAINT user_userId_unique IF NOT EXISTS 
FOR (u:User) REQUIRE u.userId IS UNIQUE;

CREATE CONSTRAINT group_groupId_unique IF NOT EXISTS 
FOR (g:Group) REQUIRE g.groupId IS UNIQUE;

CREATE CONSTRAINT page_pageId_unique IF NOT EXISTS 
FOR (p:Page) REQUIRE p.pageId IS UNIQUE;

// Verify
SHOW INDEXES;
SHOW CONSTRAINTS;
```

### Bước 7: Testing

#### 7.1. Unit Tests
```java
@SpringBootTest
@AutoConfigureDataNeo4j
class UserRelationshipServiceTest {
    
    @Autowired
    private UserRelationshipService service;
    
    @Test
    void testSendFriendRequest() {
        // Test implementation
    }
    
    @Test
    void testGetMutualFriends() {
        // Test implementation
    }
}
```

#### 7.2. Integration Tests
```java
@SpringBootTest
@Testcontainers
class UserRelationshipIntegrationTest {
    
    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:latest");
    
    @Test
    void testFullFriendshipFlow() {
        // Test implementation
    }
}
```

## 📊 Ví dụ Cypher Queries

### Tìm bạn bè
```cypher
MATCH (u:User {userId: 'user123'})-[r:FRIEND_WITH {status: 'ACCEPTED'}]-(friend:User)
RETURN friend, r
```

### Tìm bạn chung
```cypher
MATCH (u1:User {userId: 'user123'})-[:FRIEND_WITH]-(mutual:User)-[:FRIEND_WITH]-(u2:User {userId: 'user456'})
RETURN mutual
```

### Gợi ý kết bạn
```cypher
MATCH (me:User {userId: 'user123'})-[:FRIEND_WITH]-(friend:User)-[:FRIEND_WITH]-(fof:User)
WHERE NOT (me)-[:FRIEND_WITH]-(fof) 
  AND NOT (me)-[:BLOCKED]-(fof)
  AND me <> fof
RETURN fof, COUNT(friend) as mutualFriendsCount
ORDER BY mutualFriendsCount DESC
LIMIT 10
```

### Gợi ý nhóm
```cypher
MATCH (me:User {userId: 'user123'})-[:FRIEND_WITH]-(friend:User)-[:MEMBER_OF]->(group:Group)
WHERE NOT (me)-[:MEMBER_OF|BANNED_FROM]->(group)
RETURN group, COUNT(friend) as friendsInGroup
ORDER BY friendsInGroup DESC
LIMIT 10
```

## 🛠️ Công cụ hữu ích

### Neo4j Browser
- URL: http://localhost:7474
- Dùng để query và visualize graph database

### Neo4j Desktop
- Download: https://neo4j.com/download/
- GUI application để quản lý Neo4j databases

### Cypher Query Language
- Cheat sheet: https://neo4j.com/docs/cypher-cheat-sheet/

## 📚 Tài liệu bổ sung

1. **ENTITIES_SUMMARY.md** - Danh sách đầy đủ các entities
2. **RELATIONSHIP_ENTITIES_README.md** - Chi tiết về từng entity
3. **GRAPH_STRUCTURE_DIAGRAM.md** - Sơ đồ cấu trúc và queries

## ❓ FAQ

**Q: Làm sao để biết 2 user có phải bạn bè không?**
A: Kiểm tra xem có relationship `FRIEND_WITH` với `status = 'ACCEPTED'` giữa 2 user hay không.

**Q: Người lạ là gì?**
A: Người lạ là user không có bất kỳ relationship nào (FRIEND_WITH, BLOCKED, FOLLOWING) với user hiện tại.

**Q: Có nên lưu relationship 2 chiều cho bạn bè không?**
A: Không cần. Neo4j hỗ trợ query bidirectional relationship. Chỉ cần 1 relationship FRIEND_WITH là đủ.

**Q: Làm sao để xóa một relationship?**
A: Nên sử dụng "soft delete" bằng cách set `isActive = false` thay vì xóa cứng.

## 🎯 Roadmap

- [ ] Implement Repositories
- [ ] Implement Services
- [ ] Implement Controllers
- [ ] Write Unit Tests
- [ ] Write Integration Tests
- [ ] Add Validation
- [ ] Add Exception Handling
- [ ] Add Logging
- [ ] Add Metrics
- [ ] Performance Optimization
- [ ] Documentation (Swagger)
- [ ] CI/CD Pipeline

## 📞 Support

Nếu có vấn đề, vui lòng tạo issue hoặc liên hệ team.

---

**Happy Coding! 🚀**
