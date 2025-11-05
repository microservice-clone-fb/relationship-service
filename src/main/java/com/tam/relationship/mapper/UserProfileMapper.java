package com.tam.relationship.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.tam.relationship.dto.request.ProfileCreationRequest;
import com.tam.relationship.dto.request.UpdateProfileRequest;
import com.tam.relationship.dto.response.UserProfileResponse;
import com.tam.relationship.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileReponse(UserProfile entity);

    void update(@MappingTarget UserProfile entity, UpdateProfileRequest request);
}
