package com.tam.relationship.entity;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.UserPageRelationType;

import lombok.*;

/**
 * Quan hệ User-Page: follower, admin, editor, moderator
 * Thay thế cho bảng user_page_join tự sinh
 */
@Entity
@Table(
        name = "user_page_join",
        indexes = {
            @Index(name = "idx_up_user_type_status", columnList = "user_id, relation_type, status, is_active"),
            @Index(name = "idx_up_page_type_status", columnList = "page_id, relation_type, status, is_active"),
            @Index(name = "idx_up_user_page", columnList = "user_id, page_id"),
            @Index(name = "idx_up_created_at", columnList = "created_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageRelationship extends BaseRelationship {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private UserPageRelationType relationType;

    // Các trường bổ sung
    @Column(name = "notification_enabled")
    @Builder.Default
    private Boolean notificationEnabled = true;

    @Column(name = "assigned_by")
    private String assignedBy; // User ID người gán role

    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions; // JSON: {canPost: true, canDelete: false, ...}

    @Column(name = "ban_reason")
    private String banReason; // Lý do ban

    @Column(name = "is_verified_follower")
    @Builder.Default
    private Boolean isVerifiedFollower = false; // Fan verified

    // Helper methods
    public boolean hasAdminPrivileges() {
        return relationType == UserPageRelationType.ADMIN;
    }

    public boolean canManageContent() {
        return relationType == UserPageRelationType.ADMIN
                || relationType == UserPageRelationType.EDITOR
                || relationType == UserPageRelationType.MODERATOR;
    }
}
