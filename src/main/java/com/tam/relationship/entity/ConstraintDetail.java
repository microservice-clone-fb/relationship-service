package com.tam.relationship.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.ConstraintType;
import com.tam.relationship.utils.AuditListener;

import lombok.*;

/**
 * Bảng độc lập lưu trữ chi tiết của các mối quan hệ
 * Sử dụng id1 và id2 để query thông tin chi tiết
 */
@Entity
@Table(
        name = "constraint_details",
        indexes = {
            @Index(name = "idx_constraint_id1_id2", columnList = "id1, id2"),
            @Index(name = "idx_constraint_type", columnList = "constraint_type"),
            @Index(name = "idx_constraint_created_at", columnList = "created_at")
        })
@EntityListeners(AuditListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstraintDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id1", nullable = false)
    private String id1; // Entity 1 ID

    @Column(name = "id2", nullable = false)
    private String id2; // Entity 2 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "constraint_type", nullable = false)
    private ConstraintType constraintType;

    @Column(name = "status")
    private String status; // PENDING, ACCEPTED, REJECTED, ACTIVE, BANNED, etc.

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "relation_detail", columnDefinition = "TEXT")
    private String relationDetail; // JSON: {closeFriend: true, reason: "...", etc}

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "history", columnDefinition = "TEXT")
    private String history;

    public void addHistoryEntry(String entry) {
        if (this.history == null || this.history.isEmpty()) {
            this.history = entry;
        } else {
            this.history = this.history + "\n" + entry;
        }
    }
}
