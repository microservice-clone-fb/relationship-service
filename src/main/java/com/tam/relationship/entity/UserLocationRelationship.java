package com.tam.relationship.entity;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.UserLocationRelationType;

import lombok.*;

/**
 * Quan hệ User-Location: sống ở, đã sống, thăm quan
 * Thay thế cho bảng user_location_join tự sinh
 */
@Entity
@Table(
        name = "user_location_join",
        indexes = {
            @Index(name = "idx_ul_user_type_status", columnList = "user_id, relation_type, status, is_active"),
            @Index(name = "idx_ul_location_type_status", columnList = "location_id, relation_type, status, is_active"),
            @Index(name = "idx_ul_user_location", columnList = "user_id, location_id"),
            @Index(name = "idx_ul_created_at", columnList = "created_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationRelationship extends BaseRelationship {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false)
    private UserLocationRelationType relationType;

    // Các trường bổ sung
    @Column(name = "year_from")
    private Integer yearFrom; // Năm bắt đầu

    @Column(name = "year_to")
    private Integer yearTo; // Năm kết thúc

    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = true; // Hiển thị công khai

    @Column(name = "description")
    private String description; // Mô tả thêm

    // Helper methods
    public boolean isCurrent() {
        return relationType == UserLocationRelationType.LIVING
                || relationType == UserLocationRelationType.STUDYING
                || relationType == UserLocationRelationType.WORKPLACE;
    }
}
