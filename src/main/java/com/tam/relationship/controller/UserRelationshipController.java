package com.tam.relationship.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tam.relationship.dto.ApiResponse;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestCancelRequest;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestRespondRequest;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestSendRequest;
import com.tam.relationship.dto.request.relationshipuser.UnfriendRequest;
import com.tam.relationship.dto.response.relationshipuser.IncomingFriendRequestsResponse;
import com.tam.relationship.dto.response.relationshipuser.OutgoingFriendRequestsResponse;
import com.tam.relationship.dto.response.relationshipuser.RelationshipUserResponse;
import com.tam.relationship.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserRelationshipController {
    UserService userService;

    @GetMapping("/all-relationship/{userId}")
    ApiResponse<RelationshipUserResponse> getAllRelationshipBetweenUserAndUser(@PathVariable String userId) {
        RelationshipUserResponse response = userService.getAllUserUserRelationshipByUserId(userId);
        return ApiResponse.<RelationshipUserResponse>builder().result(response).build();
    }

    @PostMapping("/friend-requests/send")
    ApiResponse<String> sendFriendRequest(@Valid @RequestBody FriendRequestSendRequest request) {
        userService.sendFriendRequest(request);
        return ApiResponse.<String>builder().result("Friend request sent").build();
    }

    @PostMapping("/friend-requests/cancel")
    ApiResponse<String> cancelFriendRequest(@Valid @RequestBody FriendRequestCancelRequest request) {
        userService.cancelFriendRequest(request);
        return ApiResponse.<String>builder().result("Friend request cancelled").build();
    }

    @PostMapping("/friend-requests/respond")
    ApiResponse<String> respondFriendRequest(@Valid @RequestBody FriendRequestRespondRequest request) {
        userService.respondFriendRequest(request);
        return ApiResponse.<String>builder().result("Friend request updated").build();
    }

    @PostMapping("/friendships/remove")
    ApiResponse<String> unfriend(@Valid @RequestBody UnfriendRequest request) {
        userService.unfriend(request);
        return ApiResponse.<String>builder().result("Friend removed").build();
    }

    @GetMapping("/friend-requests/incoming/{userId}")
    ApiResponse<IncomingFriendRequestsResponse> getIncomingFriendRequests(@PathVariable String userId) {
        IncomingFriendRequestsResponse response = userService.getIncomingFriendRequests(userId);
        return ApiResponse.<IncomingFriendRequestsResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/friend-requests/outgoing/{userId}")
    ApiResponse<OutgoingFriendRequestsResponse> getOutgoingFriendRequests(@PathVariable String userId) {
        OutgoingFriendRequestsResponse response = userService.getOutgoingFriendRequests(userId);
        return ApiResponse.<OutgoingFriendRequestsResponse>builder()
                .result(response)
                .build();
    }
}
