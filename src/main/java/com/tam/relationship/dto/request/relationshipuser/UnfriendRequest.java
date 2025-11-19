package com.tam.relationship.dto.request.relationshipuser;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnfriendRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String friendId;
}
