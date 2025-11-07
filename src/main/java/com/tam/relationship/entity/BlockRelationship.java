package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ chặn giữa hai User
 * User này chặn User kia (one-way relationship)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class BlockRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("blockedAt")
    LocalDateTime blockedAt; // Thời điểm chặn

    @Property("reason")
    String reason; // Lý do chặn

    @Property("isActive")
    Boolean isActive; // Trạng thái chặn (có thể bỏ chặn)

    User blockedUser;
}
