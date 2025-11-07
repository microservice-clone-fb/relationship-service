package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ gia đình giữa hai User
 * Đại diện cho các mối quan hệ huyết thống hoặc hôn nhân
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class FamilyRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("relationType")
    String relationType; // PARENT, CHILD, SIBLING, SPOUSE, GRANDPARENT, GRANDCHILD, UNCLE, AUNT, COUSIN,
    // etc.

    @Property("createdAt")
    LocalDateTime createdAt;

    @Property("isPublic")
    Boolean isPublic; // Có công khai mối quan hệ không

    @Property("confirmedBy")
    String confirmedBy; // User ID của người xác nhận mối quan hệ

    @Property("status")
    String status; // PENDING, CONFIRMED, REJECTED

    User familyMember;
}
