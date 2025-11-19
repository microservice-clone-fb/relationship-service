package com.tam.relationship.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PageResponse {
    private String id;
    private String name;
    private String description;
    private String category;
    private String website;
    private String phone;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
