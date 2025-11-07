package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ bị ban giữa User và Group
 * User bị cấm tham gia Group
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupBanRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("bannedAt")
    LocalDateTime bannedAt; // Thời điểm bị ban

    @Property("bannedBy")
    String bannedBy; // User ID của người ban

    @Property("reason")
    String reason; // Lý do bị ban

    @Property("isPermanent")
    Boolean isPermanent; // Ban vĩnh viễn hay tạm thời

    @Property("banExpiresAt")
    LocalDateTime banExpiresAt; // Thời điểm hết hạn ban (nếu tạm thời)

    @Property("canAppeal")
    Boolean canAppeal; // Có thể khiếu nại không

    @Property("isActive")
    Boolean isActive; // Trạng thái ban

    Group group;
}
