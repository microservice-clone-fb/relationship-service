package com.tam.relationship.entity;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.UserUserRelationType;

import lombok.*;

/**
 * Quan hệ User-User: bạn bè, chặn, hẹn hò, gia đình, đồng nghiệp
 * Thay thế cho bảng user_user_join tự sinh
 */
@Entity
@Table(
        name = "user_user_join",
        indexes = {
            @Index(name = "idx_uu_user1_type_status", columnList = "user1_id, relation_type, status, is_active"),
            @Index(name = "idx_uu_user2_type_status", columnList = "user2_id, relation_type, status, is_active"),
            @Index(name = "idx_uu_bidirectional", columnList = "user1_id, user2_id, relation_type"),
            @Index(name = "idx_uu_created_at", columnList = "created_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUserRelationship extends BaseRelationship {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private UserUserRelationType relationType;

    // Các trường bổ sung cho từng loại quan hệ
    @Column(name = "is_close_friend")
    @Builder.Default
    private Boolean isCloseFriend = false; // Cho FRIEND

    @Column(name = "block_reason")
    private String blockReason; // Cho BLOCKED

    @Column(name = "family_relation")
    private String familyRelation; // Cho FAMILY: father, mother, sibling...

    @Column(name = "notification_enabled")
    @Builder.Default
    private Boolean notificationEnabled = true;

    // Helper methods
    public boolean isBidirectional() {
        return relationType == UserUserRelationType.FRIEND
                || relationType == UserUserRelationType.DATING
                || relationType == UserUserRelationType.FAMILY
                || relationType == UserUserRelationType.COLLEAGUE;
    }
}
