package com.tam.relationship.entity;

import java.time.LocalDateTime;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import org.springframework.data.neo4j.core.schema.Property;

import com.tam.relationship.utils.AuditListener;

import lombok.Getter;
import lombok.Setter;

// @Deprecated
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditListener.class)
public abstract class AuditableBaseEntity {

    @Property("createdAt")
    private LocalDateTime createdAt;

    @Property("updatedAt")
    private LocalDateTime updatedAt;

    @Property("createdBy")
    private String createdBy;

    @Property("updatedBy")
    private String updatedBy;

    @Property("isActive")
    private Boolean isActive = true;

    @PostLoad
    @PrePersist
    public void setTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }
}
