package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ quản lý Page
 * User là người quản lý/admin của Page
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class PageAdminRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("role")
    String role; // OWNER, ADMIN, EDITOR, MODERATOR, ADVERTISER, ANALYST

    @Property("assignedAt")
    LocalDateTime assignedAt; // Thời điểm được gán vai trò

    @Property("assignedBy")
    String assignedBy; // User ID của người gán vai trò

    @Property("permissions")
    String permissions; // Danh sách quyền (JSON format)

    @Property("isActive")
    Boolean isActive;

    Page page;
}
