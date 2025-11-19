package com.tam.relationship.entity;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.UserGroupRelationType;

import lombok.*;

/**
 * Quan hệ User-Group: thành viên, admin, moderator
 * Thay thế cho bảng user_group_join tự sinh
 */
@Entity
@Table(
        name = "user_group_join",
        indexes = {
                @Index(name = "idx_ug_user_type_status", columnList = "user_id, relation_type, status, is_active"),
                @Index(name = "idx_ug_group_type_status", columnList = "group_id, relation_type, status, is_active"),
                @Index(name = "idx_ug_user_group", columnList = "user_id, group_id"),
                @Index(name = "idx_ug_created_at", columnList = "created_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRelationship extends BaseRelationship {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private UserGroupRelationType relationType;

    // Các trường bổ sung
    @Column(name = "join_question_answer", columnDefinition = "TEXT")
    private String joinQuestionAnswer; // Câu trả lời khi xin vào group

    @Column(name = "invitation_sent_by")
    private String invitationSentBy; // User ID người mời

    @Column(name = "approved_by")
    private String approvedBy; // User ID người duyệt

    @Column(name = "ban_reason")
    private String banReason; // Lý do ban

    @Column(name = "notification_enabled")
    @Builder.Default
    private Boolean notificationEnabled = true;

    // Helper methods
    public boolean hasAdminPrivileges() {
        return relationType == UserGroupRelationType.ADMIN;
    }

    public boolean hasModeratorPrivileges() {
        return relationType == UserGroupRelationType.ADMIN || relationType == UserGroupRelationType.MODERATOR;
    }
}
