package com.tam.relationship.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "old_image_uploaded")
public class OldImageUploaded {
    String id;
    String userId;
    String imageUrl;
    String imageType; // e.g., "avatar", "wallAvatar"
    long uploadedAt;
}
