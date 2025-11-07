package com.tam.relationship.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Đại diện cho người dùng trong hệ thống Neo4j
 * Node này lưu trữ thông tin cơ bản và các mối quan hệ với user khác và group
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("User")
public class User {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("userId")
    String userId; // ID từ user-service, lấy profile từ profile-service bằng id này

    // Mối quan hệ: User -> User (Bạn bè)
    @Relationship(type = "FRIEND_WITH", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<FriendRelationship> friends = new HashSet<>();

    // Mối quan hệ: User -> User (Hẹn hò)
    @Relationship(type = "DATING_WITH", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<DatingRelationship> dating = new HashSet<>();

    // Mối quan hệ: User -> User (Chặn)
    @Relationship(type = "BLOCKED", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<BlockRelationship> blockedUsers = new HashSet<>();

    // Mối quan hệ: User -> User (Hạn chế)
    @Relationship(type = "RESTRICTED", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<RestrictedRelationship> restrictedUsers = new HashSet<>();

    // Mối quan hệ: User -> User (Theo dõi)
    @Relationship(type = "FOLLOWING", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<FollowRelationship> following = new HashSet<>();

    // Mối quan hệ: User -> Group (Thành viên)
    @Relationship(type = "MEMBER_OF", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<GroupMemberRelationship> memberOfGroups = new HashSet<>();

    // Mối quan hệ: User -> Group (Quản trị viên)
    @Relationship(type = "ADMIN_OF", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<GroupAdminRelationship> adminOfGroups = new HashSet<>();

    // Mối quan hệ: User -> Group (Người kiểm duyệt)
    @Relationship(type = "MODERATOR_OF", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<GroupModeratorRelationship> moderatorOfGroups = new HashSet<>();

    // Mối quan hệ: User -> Group (Người tạo)
    @Relationship(type = "CREATOR_OF", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<GroupCreatorRelationship> createdGroups = new HashSet<>();

    // Mối quan hệ: User -> Group (Bị ban)
    @Relationship(type = "BANNED_FROM", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<GroupBanRelationship> bannedFromGroups = new HashSet<>();

    // Mối quan hệ: User -> User (Người thân)
    @Relationship(type = "FAMILY_WITH", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<FamilyRelationship> familyMembers = new HashSet<>();

    // Mối quan hệ: User -> Location (Liên kết với địa điểm)
    @Relationship(type = "RELATED_TO_LOCATION", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<UserLocationRelationship> locations = new HashSet<>();

    // Mối quan hệ: User -> * (Báo cáo vi phạm)
    @Relationship(type = "REPORTED", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    Set<ReportRelationship> reports = new HashSet<>();
}
