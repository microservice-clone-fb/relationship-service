package com.tam.relationship.dto.response.relationshipuser;

import java.util.Set;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomingFriendRequestsResponse {
    String userId;
    Set<String> incomingFriendRequests;
}
