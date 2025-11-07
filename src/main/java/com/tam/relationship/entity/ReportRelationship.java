package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ báo cáo/report giữa User và User/Group/Page
 * User báo cáo vi phạm về user/group/page khác
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class ReportRelationship {

    @Id
    String id;

    @Property("reportedAt")
    LocalDateTime reportedAt;

    @Property("targetType")
    String targetType; // USER, GROUP, PAGE, POST, COMMENT

    @Property("targetId")
    String targetId; // ID của đối tượng bị report

    @Property("reason")
    String reason; // SPAM, HARASSMENT, FAKE_ACCOUNT, INAPPROPRIATE_CONTENT, etc.

    @Property("description")
    String description; // Mô tả chi tiết

    @Property("status")
    String status; // PENDING, REVIEWING, RESOLVED, DISMISSED

    @Property("reviewedBy")
    String reviewedBy; // Admin ID xem xét

    @Property("reviewedAt")
    LocalDateTime reviewedAt;

    @Property("resolution")
    String resolution; // Kết quả xử lý

    // Không cần TargetNode vì có thể là nhiều loại khác nhau (User/Group/Page)
    // Dùng targetType và targetId để xác định
}
