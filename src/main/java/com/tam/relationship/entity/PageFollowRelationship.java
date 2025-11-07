package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ theo dõi Page
 * User theo dõi một Page
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class PageFollowRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("followedAt")
    LocalDateTime followedAt; // Thời điểm theo dõi

    @Property("notificationsEnabled")
    Boolean notificationsEnabled; // Có bật thông báo không

    @Property("notificationLevel")
    String notificationLevel; // ALL, HIGHLIGHTS, OFF

    Page page;
}
