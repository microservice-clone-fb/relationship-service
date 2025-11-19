package com.tam.relationship.mapper;

import org.springframework.stereotype.Component;

import com.tam.relationship.dto.request.PageRequest;
import com.tam.relationship.dto.response.PageResponse;
import com.tam.relationship.entity.Page;

@Component
public class PageMapper {

    public Page toEntity(PageRequest request) {
        return Page.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .website(request.getWebsite())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
    }

    public PageResponse toResponse(Page entity) {
        PageResponse response = new PageResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCategory(entity.getCategory());
        response.setWebsite(entity.getWebsite());
        response.setPhone(entity.getPhone());
        response.setEmail(entity.getEmail());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setCreatedBy(entity.getCreatedBy());
        response.setUpdatedBy(entity.getUpdatedBy());
        return response;
    }

    public void updateEntity(Page entity, PageRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setCategory(request.getCategory());
        entity.setWebsite(request.getWebsite());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
    }
}
