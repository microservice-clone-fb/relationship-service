package com.tam.relationship.dto.request.relationshipuser;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestCancelRequest {

    @NotBlank
    private String requesterId;

    @NotBlank
    private String targetUserId;
}
