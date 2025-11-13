package com.tam.relationship.entity.enums;

public enum ConstraintType {
    // User-User relationships
    FRIEND_WITH,
    DATING_WITH,
    FAMILY_WITH,
    FOLLOWING,
    BLOCKED,
    RESTRICTED,

    // User-Group relationships
    USER_IN_GROUP,

    // User-Page relationships
    USER_PAGE_ADMIN,
    USER_PAGE_FOLLOW,
    USER_PAGE_LIKE,

    // User-Location relationships
    USER_LOCATION,

    // Other
    INVITATION,
    REPORT
}
