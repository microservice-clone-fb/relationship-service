package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ đơn giản giữa User và Location
 * Đại diện cho bất kỳ liên kết nào giữa người dùng và địa điểm
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class UserLocationRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("type")
    String type; // LIVES_IN, STUDIED_AT, WORKS_AT, VISITED, HOMETOWN, etc.

    @Property("startDate")
    LocalDateTime startDate; // Thời điểm bắt đầu (tùy chọn)

    @Property("endDate")
    LocalDateTime endDate; // Thời điểm kết thúc (tùy chọn)

    @Property("isCurrent")
    Boolean isCurrent; // Có hiện tại không (đang sống/làm việc/học tại đây)

    @Property("description")
    String description; // Mô tả thêm (vị trí công việc, chuyên ngành, v.v.)

    @Property("isPublic")
    Boolean isPublic; // Có công khai không

    @Property("metadata")
    String metadata; // Thông tin bổ sung dạng JSON (linh hoạt cho mọi trường hợp)

    @Property("createdAt")
    LocalDateTime createdAt;

    @TargetNode
    Location location;
}
