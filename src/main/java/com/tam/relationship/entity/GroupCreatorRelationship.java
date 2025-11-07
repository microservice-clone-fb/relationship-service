package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ người tạo giữa User và Group
 * User là người tạo ra Group (chỉ có duy nhất 1 người tạo)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupCreatorRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("createdAt")
    LocalDateTime createdAt; // Thời điểm tạo nhóm

    @Property("canTransferOwnership")
    Boolean canTransferOwnership; // Có thể chuyển quyền sở hữu nhóm không

    Group group;
}
