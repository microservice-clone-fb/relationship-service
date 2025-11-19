package com.tam.relationship.mapper;

import org.springframework.stereotype.Component;

import com.tam.relationship.dto.request.GroupRequest;
import com.tam.relationship.dto.response.GroupResponse;
import com.tam.relationship.entity.Group;

@Component
public class GroupMapper {

    public Group toEntity(GroupRequest request) {
        return Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public GroupResponse toResponse(Group entity) {
        GroupResponse response = new GroupResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setIsActive(entity.getIsActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setCreatedBy(entity.getCreatedBy());
        response.setUpdatedBy(entity.getUpdatedBy());
        return response;
    }

    public void updateEntity(Group entity, GroupRequest request) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            entity.setIsActive(request.getIsActive());
        }
    }
}
