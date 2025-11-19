package com.tam.relationship.mapper;

import org.springframework.stereotype.Component;

import com.tam.relationship.dto.request.LocationRequest;
import com.tam.relationship.dto.response.LocationResponse;
import com.tam.relationship.entity.Location;

@Component
public class LocationMapper {

    public Location toEntity(LocationRequest request) {
        return Location.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }

    public LocationResponse toResponse(Location entity) {
        LocationResponse response = new LocationResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setAddress(entity.getAddress());
        response.setCity(entity.getCity());
        response.setCountry(entity.getCountry());
        response.setLatitude(entity.getLatitude());
        response.setLongitude(entity.getLongitude());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setCreatedBy(entity.getCreatedBy());
        response.setUpdatedBy(entity.getUpdatedBy());
        return response;
    }

    public void updateEntity(Location entity, LocationRequest request) {
        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
        entity.setCity(request.getCity());
        entity.setCountry(request.getCountry());
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
    }
}
