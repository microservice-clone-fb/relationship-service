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
import com.tam.relationship.entity.BaseRelationship;
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
    public void prePersist(Object entity) {
        applyAuditFields(entity, "CREATED");
    }

    /**
     * Xử lý trước khi update
     */
    @PreUpdate
    public void preUpdate(Object entity) {
        applyAuditFields(entity, "UPDATED");
    }

    private void applyAuditFields(Object target, String action) {
        if (!(target instanceof BaseEntity) && !(target instanceof BaseRelationship)) {
            return;
        }

        AuditService auditService = getAuditService();

        Instant now = Instant.now();
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String currentUser = auditService != null ? auditService.getCurrentUsername() : "SYSTEM";

        if (target instanceof BaseEntity baseEntity) {
            applyAuditToBaseEntity(baseEntity, action, now, nowDateTime, currentUser, auditService);
        } else if (target instanceof BaseRelationship baseRelationship) {
            applyAuditToBaseRelationship(baseRelationship, action, now, nowDateTime, currentUser, auditService);
        }
    }

    private void applyAuditToBaseEntity(
            BaseEntity entity,
            String action,
            Instant now,
            LocalDateTime nowDateTime,
            String currentUser,
            AuditService auditService) {
        if ("CREATED".equals(action)) {
            entity.setCreatedAt(nowDateTime);
            entity.setCreatedBy(currentUser);
            if (entity.getIsActive() == null) {
                entity.setIsActive(true);
            }
        }

        entity.setUpdatedAt(nowDateTime);
        entity.setUpdatedBy(currentUser);

        appendHistory(entity::addHistoryEntry, action, now, nowDateTime, currentUser, auditService);
    }

    private void applyAuditToBaseRelationship(
            BaseRelationship relationship,
            String action,
            Instant now,
            LocalDateTime nowDateTime,
            String currentUser,
            AuditService auditService) {
        if ("CREATED".equals(action)) {
            relationship.setCreatedAt(nowDateTime);
            relationship.setCreatedBy(currentUser);
            if (relationship.getIsActive() == null) {
                relationship.setIsActive(true);
            }
        }

        relationship.setUpdatedAt(nowDateTime);
        relationship.setUpdatedBy(currentUser);

        appendHistory(relationship::addHistoryEntry, action, now, nowDateTime, currentUser, auditService);
    }

    private void appendHistory(
            java.util.function.Consumer<String> historyConsumer,
            String action,
            Instant now,
            LocalDateTime nowDateTime,
            String currentUser,
            AuditService auditService) {
        if (auditService != null) {
            String historyEntry = auditService.createHistoryEntry(currentUser, action, now);
            historyConsumer.accept(historyEntry);
        } else {
            historyConsumer.accept(String.format("[%s] %s by %s", nowDateTime, action, currentUser));
        }
    }
}
