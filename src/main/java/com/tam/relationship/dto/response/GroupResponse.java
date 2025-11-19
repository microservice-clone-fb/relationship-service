package com.tam.relationship.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GroupResponse {
    private String id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
