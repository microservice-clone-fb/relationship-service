package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ đã ghé thăm giữa User và Location
 * User đã check-in hoặc ghé thăm địa điểm
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class VisitedRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("visitedAt")
    LocalDateTime visitedAt; // Thời điểm ghé thăm

    @Property("visitCount")
    Integer visitCount; // Số lần ghé thăm

    @Property("rating")
    Integer rating; // Đánh giá (1-5 sao)

    @Property("review")
    String review; // Nhận xét

    @Property("photos")
    String photos; // Danh sách ID ảnh (JSON array)

    @Property("isPublic")
    Boolean isPublic;

    @Property("taggedUsers")
    String taggedUsers; // Danh sách user ID được tag (JSON array)

    @TargetNode
    Location location;
}
