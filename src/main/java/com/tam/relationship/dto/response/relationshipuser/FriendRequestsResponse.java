package com.tam.relationship.dto.response.relationshipuser;

import java.util.Set;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestsResponse {
    String userId;
    Set<String> incomingFriendRequests;
    Set<String> outgoingFriendRequests;
}
