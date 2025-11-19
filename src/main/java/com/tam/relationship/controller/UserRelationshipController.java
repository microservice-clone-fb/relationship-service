package com.tam.relationship.controller;

import com.tam.relationship.dto.ApiResponse;
import com.tam.relationship.dto.response.relationshipuser.RelationshipUserResponse;
import com.tam.relationship.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserRelationshipController {
    UserService userService;
    @GetMapping("/all-relationship/{userId}")
    ApiResponse<RelationshipUserResponse> getAllRelationshipBetweenUserAndUser(@PathVariable String userId){
        RelationshipUserResponse response = userService.getAllUserUserRelationshipByUserId(userId);
        return ApiResponse.<RelationshipUserResponse>builder().result(response).build();
    }


}
