package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ yêu cầu tham gia nhóm
 * User yêu cầu tham gia một Group (nhóm Private/Secret)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class GroupJoinRequestRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("requestedAt")
    LocalDateTime requestedAt; // Thời điểm yêu cầu

    @Property("status")
    String status; // PENDING, APPROVED, REJECTED

    @Property("reviewedBy")
    String reviewedBy; // User ID của admin/moderator xét duyệt

    @Property("reviewedAt")
    LocalDateTime reviewedAt; // Thời điểm xét duyệt

    @Property("rejectionReason")
    String rejectionReason; // Lý do từ chối (nếu có)

    @Property("answers")
    String answers; // Câu trả lời cho các câu hỏi tham gia nhóm (JSON format)

    Group group;
}
