package com.tam.relationship.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PageRequest {
    @NotBlank(message = "Page name is required")
    private String name;

    private String description;
    private String category;
    private String website;
    private String phone;
    private String email;
    private Boolean isActive = true;
}
