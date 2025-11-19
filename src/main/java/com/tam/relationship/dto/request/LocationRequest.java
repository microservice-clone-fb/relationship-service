package com.tam.relationship.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LocationRequest {
    @NotBlank(message = "Location name is required")
    private String name;

    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;
    private Boolean isActive = true;
}
