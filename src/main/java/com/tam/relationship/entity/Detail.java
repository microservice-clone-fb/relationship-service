package com.tam.relationship.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.tam.relationship.entity.enums.DetailType;
import com.tam.relationship.utils.AuditListener;

import lombok.*;

/**
 * Bảng Detail để lưu trữ các thông tin chi tiết
 * Không có mối quan hệ với bất kỳ entity nào
 */
@Entity
@Table(
        name = "details",
        indexes = {
            @Index(name = "idx_detail_type", columnList = "type"),
            @Index(name = "idx_detail_created_at", columnList = "created_at")
        })
@EntityListeners(AuditListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
@SuppressWarnings("Entity này ko dùng nưa nhưng ko nỡ xóa")
public class Detail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DetailType type;

    @Column(name = "data", columnDefinition = "TEXT")
    private String data; // JSON hoặc text data

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
