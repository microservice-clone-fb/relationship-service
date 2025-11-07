package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ hẹn hò giữa hai User
 * Đại diện cho tình trạng mối quan hệ tình cảm
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class DatingRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("status")
    String status; // SINGLE, IN_RELATIONSHIP, ENGAGED, MARRIED, COMPLICATED, SEPARATED, DIVORCED,
    // WIDOWED

    @Property("startedAt")
    LocalDateTime startedAt; // Thời điểm bắt đầu mối quan hệ

    @Property("isPublic")
    Boolean isPublic; // Có công khai mối quan hệ không

    @Property("anniversaryDate")
    LocalDateTime anniversaryDate; // Ngày kỷ niệm

    @Property("updatedAt")
    LocalDateTime updatedAt;

    User partner;
}
