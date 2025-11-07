package com.tam.relationship.entity.enums;

/**
 * Enum định nghĩa các loại mối quan hệ gia đình
 */
public enum FamilyRelationType {
    // Mối quan hệ trực tiếp
    PARENT, // Cha/Mẹ
    CHILD, // Con
    SIBLING, // Anh/Chị/Em ruột
    SPOUSE, // Vợ/Chồng

    // Mối quan hệ thế hệ trước
    GRANDPARENT, // Ông/Bà
    GRANDCHILD, // Cháu

    // Mối quan hệ bên ngoài
    UNCLE, // Chú/Bác/Cậu
    AUNT, // Dì/Cô/Thím
    COUSIN, // Anh/Chị/Em họ

    // Mối quan hệ họ hàng khác
    NEPHEW, // Cháu trai (con của anh chị em)
    NIECE, // Cháu gái (con của anh chị em)

    // Mối quan hệ nhà chồng/vợ
    PARENT_IN_LAW, // Bố/Mẹ chồng/vợ
    SIBLING_IN_LAW, // Anh/Chị/Em dâu/rể

    // Khác
    STEP_PARENT, // Cha/Mẹ kế
    STEP_CHILD, // Con riêng
    STEP_SIBLING, // Anh/Chị/Em kế
    OTHER // Khác
}
