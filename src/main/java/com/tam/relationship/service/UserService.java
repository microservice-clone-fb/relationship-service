package com.tam.relationship.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tam.relationship.dto.response.relationshipuser.RelationshipUserResponse;
import com.tam.relationship.entity.UserUserRelationship;
import com.tam.relationship.entity.enums.UserUserRelationType;
import com.tam.relationship.repository.UserWithUserRepository;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.tam.relationship.entity.User;
import com.tam.relationship.exception.AppException;
import com.tam.relationship.exception.ErrorCode;
import com.tam.relationship.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    UserWithUserRepository userWithUserRepository;

    public boolean createUser(String userId) {
        if (userRepository.existsById(userId)){
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        User user = User.builder().userId(userId).build();
        userRepository.save(user);
        return true;
    }


    // user-user
    // Add the list of user IDs who are following the given user
    public RelationshipUserResponse getAllUserUserRelationshipByUserId(String userId) {
        List<UserUserRelationship> listAllRelationships = userWithUserRepository
                .findAllRelationshipBetweenUserAndUserByOneUserId(userId);

        // Group relationships by type
        Set<String> friends = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.FRIEND);
        Set<String> blocked = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.BLOCKED);
        Set<String> dating = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.DATING);
        Set<String> family = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.FAMILY);
        Set<String> colleagues = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.COLLEAGUE);
        Set<String> following = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.FOLLOWING);
        Set<String> closeFriends = extractUserIdsByType(listAllRelationships, userId, UserUserRelationType.CLOSE_FRIEND);

        // Extract users who are following the given user
        Set<String> anotherUserFollowedIt = listAllRelationships.stream()
                .filter(relationship -> relationship.getRelationType() == UserUserRelationType.FOLLOWING)
                .filter(relationship -> relationship.getUser2().getId().equals(userId)) // Check if the user is being followed
                .map(relationship -> relationship.getUser1().getId()) // Get the follower's ID
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
                .anotherUserFollowedIt(anotherUserFollowedIt)
                .build();
    }

    // Helper method to extract user IDs by relationship type
    private Set<String> extractUserIdsByType(List<UserUserRelationship> relationships, String userId, UserUserRelationType type) {
        return relationships.stream()
                .filter(relationship -> relationship.getRelationType() == type)
                .map(relationship -> relationship.getUser1().getId().equals(userId)
                        ? relationship.getUser2().getId()
                        : relationship.getUser1().getId())
                .collect(Collectors.toSet());
    }
}
