package com.tam.relationship.dto.request;

import java.time.LocalDate;

import com.tam.relationship.entity.ContactInfo;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreationRequest {
    String userId;
    String firstName;
    String lastName;
    String avatarId;
    String gender;
    LocalDate dateOfBirth;
    String type;
    String bio;
    ContactInfo contactInfo;
}
