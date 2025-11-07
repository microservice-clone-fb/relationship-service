package com.tam.relationship.entity.enums;

/**
 * Enum định nghĩa các loại quyền trong nhóm
 */
public enum GroupPermission {
    // Admin permissions
    MANAGE_MEMBERS, // Quản lý thành viên
    APPROVE_POSTS, // Phê duyệt bài viết
    DELETE_POSTS, // Xóa bài viết
    EDIT_GROUP_INFO, // Chỉnh sửa thông tin nhóm
    MANAGE_ADMINS, // Quản lý admin
    MANAGE_MODERATORS, // Quản lý moderator
    DELETE_GROUP, // Xóa nhóm

    // Moderator permissions
    DELETE_COMMENTS, // Xóa bình luận
    WARN_MEMBERS, // Cảnh cáo thành viên
    BAN_MEMBERS, // Ban thành viên
    APPROVE_MEMBER_POSTS, // Phê duyệt bài viết của thành viên
    PIN_POSTS, // Ghim bài viết

    // Member permissions
    POST_IN_GROUP, // Đăng bài trong nhóm
    COMMENT, // Bình luận
    INVITE_MEMBERS // Mời thành viên
}
