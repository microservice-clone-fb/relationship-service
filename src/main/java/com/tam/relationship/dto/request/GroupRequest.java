package com.tam.relationship.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class GroupRequest {
    @NotBlank(message = "Group name is required")
    private String name;

    private String description;
    private Boolean isActive = true;
}
