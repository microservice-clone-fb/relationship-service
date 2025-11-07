package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ bạn bè giữa hai User
 * Quan hệ này là hai chiều (bidirectional)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// FriendRelationship
@RelationshipProperties
public class FriendRelationship {
    @Id
    String id;

    @Property("status")
    String status; // ✅ GIỮA (PENDING, ACCEPTED, REJECTED)

    @Property("requestInitiator")
    String requestInitiator; // ✅ GIỮA (userID từ user-service)

    @Property("requestedAt")
    LocalDateTime requestedAt; // ✅ GIỮA (metadata của relationship)

    @Property("acceptedAt")
    LocalDateTime acceptedAt; // ✅ GIỮA

    @Property("closeFriend")
    Boolean closeFriend; // ✅ GIỮA (trạng thái quan hệ)

    @Property("isBestFriend")
    Boolean isBestFriend; // ✅ GIỮA (trạng thái quan hệ)

    @Property("showInNewsfeed")
    Boolean showInNewsfeed; // Hiển thị bài viết trong newsfeed (unfollow nhưng vẫn là bạn)

    @Property("birthdayNotification")
    Boolean birthdayNotification; // Nhận thông báo sinh nhật

    // ❌ XÓA: mutualFriendsCount → query trực tiếp Neo4j
    // ❌ XÓA: friend info → join với friend-service

    @TargetNode
    User friend;
}
