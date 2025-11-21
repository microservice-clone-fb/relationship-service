package com.tam.relationship.dto.response.relationshipuser;

import java.util.Set;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutgoingFriendRequestsResponse {
    String userId;
    Set<String> outgoingFriendRequests;
}
