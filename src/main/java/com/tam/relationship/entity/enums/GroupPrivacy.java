package com.tam.relationship.entity.enums;

/**
 * Enum định nghĩa các quyền riêng tư của nhóm
 */
public enum GroupPrivacy {
    PUBLIC, // Công khai - Ai cũng có thể tìm thấy và xem nội dung
    PRIVATE, // Riêng tư - Chỉ thành viên mới xem được nội dung
    SECRET // Bí mật - Không thể tìm thấy qua tìm kiếm
}
