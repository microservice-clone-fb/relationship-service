package com.tam.relationship.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tam.relationship.dto.request.relationshipuser.FriendRequestCancelRequest;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestDecision;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestRespondRequest;
import com.tam.relationship.dto.request.relationshipuser.FriendRequestSendRequest;
import com.tam.relationship.dto.request.relationshipuser.UnfriendRequest;
import com.tam.relationship.dto.response.relationshipuser.RelationshipUserResponse;
import com.tam.relationship.entity.User;
import com.tam.relationship.entity.UserUserRelationship;
import com.tam.relationship.entity.enums.RelationshipStatus;
import com.tam.relationship.entity.enums.UserUserRelationType;
import com.tam.relationship.exception.AppException;
import com.tam.relationship.exception.ErrorCode;
import com.tam.relationship.repository.UserRepository;
import com.tam.relationship.repository.UserWithUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    UserWithUserRepository userWithUserRepository;

    public boolean createUser(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User user = User.builder().userId(userId).build();
        userRepository.save(user);
        return true;
    }

    // user-user
    public RelationshipUserResponse getAllUserUserRelationshipByUserId(String userId) {
        List<UserUserRelationship> listAllRelationships =
                userWithUserRepository.findAllRelationshipBetweenUserAndUserByOneUserId(userId);

        // Group relationships by type (only active)
        Set<String> friends = extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.FRIEND);
        Set<String> blocked = extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.BLOCKED);
        Set<String> dating = extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.DATING);
        Set<String> family = extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.FAMILY);
        Set<String> colleagues =
                extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.COLLEAGUE);
        Set<String> following =
                extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.FOLLOWING);
        Set<String> closeFriends =
                extractActiveUserIdsByType(listAllRelationships, userId, UserUserRelationType.CLOSE_FRIEND);

        // Pending friend requests
        Set<String> outgoingFriendRequests = listAllRelationships.stream()
                .filter(relationship -> relationship.getRelationType() == UserUserRelationType.FRIEND)
                .filter(relationship -> relationship.getStatus() == RelationshipStatus.PENDING)
                .filter(relationship -> Objects.equals(relationship.getUser1().getUserId(), userId))
                .map(relationship -> relationship.getUser2().getUserId())
                .collect(Collectors.toSet());

        Set<String> incomingFriendRequests = listAllRelationships.stream()
                .filter(relationship -> relationship.getRelationType() == UserUserRelationType.FRIEND)
                .filter(relationship -> relationship.getStatus() == RelationshipStatus.PENDING)
                .filter(relationship -> Objects.equals(relationship.getUser2().getUserId(), userId))
                .map(relationship -> relationship.getUser1().getUserId())
                .collect(Collectors.toSet());

        // Extract users who are following the given user
        Set<String> anotherUserFollowedIt = listAllRelationships.stream()
                .filter(relationship -> relationship.getRelationType() == UserUserRelationType.FOLLOWING)
                .filter(relationship -> relationship.getStatus() == RelationshipStatus.ACTIVE)
                .filter(relationship -> relationship.getUser2().getUserId().equals(userId))
                .map(relationship -> relationship.getUser1().getUserId())
                .collect(Collectors.toSet());

        return RelationshipUserResponse.builder()
                .userId(userId)
                .friends(friends)
                .blocked(blocked)
                .dating(dating)
                .family(family)
                .colleagues(colleagues)
                .following(following)
                .closeFriends(closeFriends)
                .incomingFriendRequests(incomingFriendRequests)
                .outgoingFriendRequests(outgoingFriendRequests)
                .anotherUserFollowedIt(anotherUserFollowedIt)
                .build();
    }

    public void sendFriendRequest(FriendRequestSendRequest request) {
        validateDistinctUsers(request.getRequesterId(), request.getTargetUserId());

        User requester = getUserByUserId(request.getRequesterId());
        User target = getUserByUserId(request.getTargetUserId());

        UserUserRelationship existingRelationship = userWithUserRepository
                .findRelationshipBetweenUsers(
                        request.getRequesterId(), request.getTargetUserId(), UserUserRelationType.FRIEND)
                .orElse(null);

        if (existingRelationship != null) {
            switch (existingRelationship.getStatus()) {
                case ACTIVE -> throw new AppException(ErrorCode.RELATIONSHIP_ALREADY_EXISTS);
                case BLOCKED -> throw new AppException(ErrorCode.USER_BLOCKED);
                case PENDING -> handlePendingRequestOnSend(request, existingRelationship);
                default -> resetRelationshipToPending(existingRelationship, requester, target, request.getMessage());
            }
            userWithUserRepository.save(existingRelationship);
            return;
        }

        UserUserRelationship relationship = UserUserRelationship.builder()
                .user1(requester)
                .user2(target)
                .relationType(UserUserRelationType.FRIEND)
                .build();

        relationship.setStatus(RelationshipStatus.PENDING);
        relationship.setIsActive(true);
        relationship.setMetadata(request.getMessage());

        userWithUserRepository.save(relationship);
    }

    public void cancelFriendRequest(FriendRequestCancelRequest request) {
        UserUserRelationship relationship = userWithUserRepository
                .findDirectedRelationship(
                        request.getRequesterId(),
                        request.getTargetUserId(),
                        UserUserRelationType.FRIEND,
                        RelationshipStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        userWithUserRepository.delete(relationship);
    }

    public void respondFriendRequest(FriendRequestRespondRequest request) {
        UserUserRelationship relationship = userWithUserRepository
                .findDirectedRelationship(
                        request.getRequesterId(),
                        request.getTargetUserId(),
                        UserUserRelationType.FRIEND,
                        RelationshipStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if (request.getDecision() == FriendRequestDecision.ACCEPT) {
            relationship.setStatus(RelationshipStatus.ACTIVE);
            relationship.setIsActive(true);
            relationship.setStartedAt(LocalDateTime.now());
            relationship.addHistoryEntry("%s_accept_%s".formatted(request.getTargetUserId(), LocalDateTime.now()));
            userWithUserRepository.save(relationship);
        } else {
            userWithUserRepository.delete(relationship);
            return;
        }
    }

    public void unfriend(UnfriendRequest request) {
        UserUserRelationship relationship = userWithUserRepository
                .findRelationshipBetweenUsers(request.getUserId(), request.getFriendId(), UserUserRelationType.FRIEND)
                .orElseThrow(() -> new AppException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (relationship.getStatus() != RelationshipStatus.ACTIVE) {
            throw new AppException(ErrorCode.NOT_FRIENDS);
        }

        userWithUserRepository.delete(relationship);
    }

    private void handlePendingRequestOnSend(
            FriendRequestSendRequest request, UserUserRelationship existingRelationship) {
        boolean oppositeDirection = existingRelationship.getUser1().getUserId().equals(request.getTargetUserId())
                && existingRelationship.getUser2().getUserId().equals(request.getRequesterId());

        if (oppositeDirection) {
            existingRelationship.setStatus(RelationshipStatus.ACTIVE);
            existingRelationship.setIsActive(true);
            existingRelationship.setStartedAt(LocalDateTime.now());
            existingRelationship.addHistoryEntry(
                    "%s_auto_accept_%s".formatted(request.getRequesterId(), LocalDateTime.now()));
        } else {
            throw new AppException(ErrorCode.RELATIONSHIP_ALREADY_EXISTS);
        }
    }

    private void resetRelationshipToPending(
            UserUserRelationship relationship, User requester, User target, String message) {
        relationship.setUser1(requester);
        relationship.setUser2(target);
        relationship.setStatus(RelationshipStatus.PENDING);
        relationship.setIsActive(true);
        relationship.setStartedAt(null);
        relationship.setEndedAt(null);
        relationship.setMetadata(message);
    }

    // Helper method to extract user IDs by relationship type
    private Set<String> extractActiveUserIdsByType(
            List<UserUserRelationship> relationships, String userId, UserUserRelationType type) {
        return relationships.stream()
                .filter(relationship -> relationship.getRelationType() == type)
                .filter(relationship -> relationship.getStatus() == RelationshipStatus.ACTIVE)
                .filter(relationship -> Boolean.TRUE.equals(relationship.getIsActive()))
                .map(relationship -> relationship.getUser1().getUserId().equals(userId)
                        ? relationship.getUser2().getUserId()
                        : relationship.getUser1().getUserId())
                .collect(Collectors.toSet());
    }

    private User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateDistinctUsers(String requesterId, String targetUserId) {
        if (requesterId.equals(targetUserId)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
    }
}
