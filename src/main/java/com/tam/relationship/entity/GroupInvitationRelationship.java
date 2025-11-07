package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ được mời vào nhóm
 * User được mời vào Group nhưng chưa chấp nhận
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupInvitationRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("invitedAt")
    LocalDateTime invitedAt; // Thời điểm được mời

    @Property("invitedBy")
    String invitedBy; // User ID của người mời

    @Property("status")
    String status; // PENDING, ACCEPTED, DECLINED, EXPIRED

    @Property("expiresAt")
    LocalDateTime expiresAt; // Thời điểm hết hạn lời mời

    @Property("respondedAt")
    LocalDateTime respondedAt; // Thời điểm phản hồi

    @Property("invitationMessage")
    String invitationMessage; // Lời nhắn kèm theo lời mời

    Group group;
}
