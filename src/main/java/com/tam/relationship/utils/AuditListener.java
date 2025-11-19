package com.tam.relationship.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tam.relationship.entity.BaseEntity;
import com.tam.relationship.service.AuditService;

/**
 * JPA Entity Listener cho audit functionality
 * Xử lý tất cả entity kế thừa từ BaseEntity
 */
@Component
public class AuditListener {

    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        AuditListener.applicationContext = context;
    }

    /**
     * Lấy AuditService từ Spring Context
     */
    private AuditService getAuditService() {
        if (applicationContext == null) {
            return null;
        }
        try {
            return applicationContext.getBean(AuditService.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Xử lý trước khi persist
     */
    @PrePersist
    public void prePersist(BaseEntity entity) {
        AuditService auditService = getAuditService();

        Instant now = Instant.now();
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String currentUser = auditService != null ? auditService.getCurrentUsername() : "SYSTEM";

        // Set audit fields
        entity.setCreatedAt(nowDateTime);
        entity.setUpdatedAt(nowDateTime);
        entity.setCreatedBy(currentUser);
        entity.setUpdatedBy(currentUser);

        if (entity.getIsActive() == null) {
            entity.setIsActive(true);
        }

        // Add history entry
        if (auditService != null) {
            String historyEntry = auditService.createHistoryEntry(currentUser, "CREATED", now);
            entity.addHistoryEntry(historyEntry);
        } else {
            entity.addHistoryEntry(String.format("[%s] CREATED by %s", nowDateTime, currentUser));
        }
    }

    /**
     * Xử lý trước khi update
     */
    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        AuditService auditService = getAuditService();

        Instant now = Instant.now();
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String currentUser = auditService != null ? auditService.getCurrentUsername() : "SYSTEM";

        // Update audit fields
        entity.setUpdatedAt(nowDateTime);
        entity.setUpdatedBy(currentUser);

        // Add history entry
        if (auditService != null) {
            String historyEntry = auditService.createHistoryEntry(currentUser, "UPDATED", now);
            entity.addHistoryEntry(historyEntry);
        } else {
            entity.addHistoryEntry(String.format("[%s] UPDATED by %s", nowDateTime, currentUser));
        }
    }
}