package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ thành viên giữa User và Group
 * User là thành viên của Group
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupMemberRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("joinedAt")
    LocalDateTime joinedAt; // Thời điểm tham gia

    @Property("status")
    String status; // PENDING, APPROVED, INVITED

    @Property("invitedBy")
    String invitedBy; // User ID của người mời (nếu có)

    @Property("notificationsEnabled")
    Boolean notificationsEnabled; // Có bật thông báo cho nhóm không

    @Property("activityScore")
    Integer activityScore; // Điểm hoạt động trong nhóm

    @Property("lastActive")
    LocalDateTime lastActive; // Lần cuối hoạt động trong nhóm

    Group group;
}
