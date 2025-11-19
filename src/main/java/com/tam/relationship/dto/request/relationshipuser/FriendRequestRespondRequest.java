package com.tam.relationship.dto.request.relationshipuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestRespondRequest {

    @NotBlank
    private String requesterId;

    @NotBlank
    private String targetUserId;

    @NotNull
    private FriendRequestDecision decision;
}
