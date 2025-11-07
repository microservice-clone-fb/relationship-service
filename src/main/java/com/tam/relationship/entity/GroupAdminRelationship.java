package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ quản trị viên giữa User và Group
 * User là admin của Group với các quyền quản lý
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupAdminRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("appointedAt")
    LocalDateTime appointedAt; // Thời điểm được bổ nhiệm

    @Property("appointedBy")
    String appointedBy; // User ID của người bổ nhiệm

    @Property("permissions")
    String permissions; // Danh sách quyền (JSON string hoặc comma-separated)
    // Ví dụ: MANAGE_MEMBERS, APPROVE_POSTS, DELETE_POSTS, EDIT_GROUP_INFO

    @Property("isActive")
    Boolean isActive; // Trạng thái hoạt động

    Group group;
}
