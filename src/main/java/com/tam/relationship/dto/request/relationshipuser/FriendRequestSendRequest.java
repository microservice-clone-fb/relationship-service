package com.tam.relationship.dto.request.relationshipuser;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestSendRequest {

    @NotBlank
    private String requesterId;

    @NotBlank
    private String targetUserId;

    private String message;
}
