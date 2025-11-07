package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ người kiểm duyệt giữa User và Group
 * User là moderator của Group với quyền kiểm duyệt nội dung
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupModeratorRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("appointedAt")
    LocalDateTime appointedAt; // Thời điểm được bổ nhiệm

    @Property("appointedBy")
    String appointedBy; // User ID của người bổ nhiệm

    @Property("permissions")
    String permissions; // Quyền kiểm duyệt (APPROVE_POSTS, DELETE_COMMENTS, WARN_MEMBERS, etc.)

    @Property("moderationCount")
    Integer moderationCount; // Số lượng bài viết đã kiểm duyệt

    @Property("isActive")
    Boolean isActive;

    Group group;
}
