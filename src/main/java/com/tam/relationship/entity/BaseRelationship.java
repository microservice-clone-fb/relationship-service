package com.tam.relationship.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.RelationshipStatus;
import com.tam.relationship.utils.AuditListener;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class cho tất cả các bảng quan hệ
 * Chứa các thuộc tính chung: status, metadata, timestamps
 */
@MappedSuperclass
@EntityListeners(AuditListener.class)
@Getter
@Setter
public abstract class BaseRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RelationshipStatus status = RelationshipStatus.ACTIVE;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON cho các thông tin bổ sung

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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
