package com.tam.relationship.dto.request.relationshipuser.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketFriendRequestSendPayload {
    private String targetUserId;
    private String message;
}
