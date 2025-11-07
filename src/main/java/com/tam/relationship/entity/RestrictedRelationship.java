package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ hạn chế giữa hai User
 * User hạn chế một user khác (restricted list)
 * Người bị hạn chế vẫn là bạn bè nhưng bị giới hạn xem nội dung
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class RestrictedRelationship {

    @Id
    String id;

    @Property("restrictedAt")
    LocalDateTime restrictedAt; // Thời điểm bị hạn chế

    @Property("reason")
    String reason; // Lý do (tùy chọn)

    @Property("restrictions")
    String restrictions; // Các giới hạn cụ thể (JSON: {posts: false, stories: true, ...})

    @Property("isActive")
    Boolean isActive; // Có đang active không

    @TargetNode
    User restrictedUser;
}
