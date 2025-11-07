package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ thích Page
 * User thích một Page
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class PageLikeRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("likedAt")
    LocalDateTime likedAt; // Thời điểm thích

    @Property("notificationsEnabled")
    Boolean notificationsEnabled; // Có bật thông báo không

    @Property("showInNewsfeed")
    Boolean showInNewsfeed; // Hiển thị trong newsfeed không

    Page page;
}
