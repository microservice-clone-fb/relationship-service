package com.tam.relationship.entity;

import org.springframework.data.neo4j.core.schema.Node;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("user_profile_contact")
public class ContactInfo {
    String userId;
    String email;
    String phoneNumber;
    String address;
    String socialMediaLinks; // JSON or comma-separated links
    String mediaLinks; // JSON or comma-separated links
}
