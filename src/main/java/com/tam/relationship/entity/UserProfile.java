package com.tam.relationship.entity;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("userId")
    String userId;

    String avatarId; // This is an ID referencing an avatar image from file-service
    String wallAvatar;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String gender;
    String type; // e.g., "user", "admin", "moderator"
    String bio;

    @Relationship(type = "HAS_CONTACT_INFO", direction = Relationship.Direction.OUTGOING)
    ContactInfo contactInfo;
}
