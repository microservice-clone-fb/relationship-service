package com.tam.relationship.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LocationResponse {
    private String id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
