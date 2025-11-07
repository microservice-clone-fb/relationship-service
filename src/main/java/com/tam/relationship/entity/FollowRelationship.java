package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ theo dõi giữa hai User
 * User này theo dõi User kia (one-way relationship)
 * Khác với bạn bè, theo dõi không cần được chấp nhận
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class FollowRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("followedAt")
    LocalDateTime followedAt; // Thời điểm bắt đầu theo dõi

    @Property("notificationsEnabled")
    Boolean notificationsEnabled; // Có bật thông báo cho người này không

    @Property("showInFeed")
    Boolean showInFeed; // Có hiển thị bài viết của người này trong newsfeed không

    @TargetNode
    User followedUser;
}
