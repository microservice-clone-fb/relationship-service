package com.tam.relationship.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import com.tam.relationship.entity.BaseEntity;
import com.tam.relationship.service.AuditService;

/**
 * JPA Entity Listener cho audit functionality
 * Xử lý tất cả entity kế thừa từ BaseEntity
 */
public class AuditListener {

    private AuditService auditService;

    public AuditListener() {}

    public AuditListener(AuditService auditService) {
        this.auditService = auditService;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Xử lý trước khi persist
     */
    @PrePersist
    public void prePersist(BaseEntity entity) {
        if (auditService == null) {
            return;
        }

        Instant now = Instant.now();
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String currentUser = auditService.getCurrentUsername();

        // Set audit fields
        entity.setCreatedAt(nowDateTime);
        entity.setUpdatedAt(nowDateTime);
        entity.setCreatedBy(currentUser);
        entity.setUpdatedBy(currentUser);
        if (entity.getIsActive() == null) {
            entity.setIsActive(true);
        }

        // Add history entry
        String historyEntry = auditService.createHistoryEntry(currentUser, "CREATED", now);
        entity.addHistoryEntry(historyEntry);
    }

    /**
     * Xử lý trước khi update
     */
    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        if (auditService == null) {
            return;
        }

        Instant now = Instant.now();
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String currentUser = auditService.getCurrentUsername();

        // Update audit fields
        entity.setUpdatedAt(nowDateTime);
        entity.setUpdatedBy(currentUser);

        // Add history entry
        String historyEntry = auditService.createHistoryEntry(currentUser, "UPDATED", now);
        entity.addHistoryEntry(historyEntry);
    }
}
