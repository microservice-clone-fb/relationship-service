package com.tam.relationship.entity;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Mối quan hệ làm việc tại giữa User và Location
 * User đang làm hoặc đã từng làm việc tại công ty/địa điểm
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RelationshipProperties
public class WorksAtRelationship {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("companyName")
    String companyName; // Tên công ty

    @Property("position")
    String position; // Vị trí công việc

    @Property("department")
    String department; // Phòng ban

    @Property("employmentType")
    String employmentType; // FULL_TIME, PART_TIME, CONTRACT, FREELANCE, INTERNSHIP

    @Property("startDate")
    LocalDateTime startDate; // Ngày bắt đầu

    @Property("endDate")
    LocalDateTime endDate; // Ngày kết thúc (nếu đã nghỉ)

    @Property("isCurrent")
    Boolean isCurrent; // Có đang làm việc không

    @Property("description")
    String description; // Mô tả công việc

    @Property("responsibilities")
    String responsibilities; // Trách nhiệm công việc

    @Property("achievements")
    String achievements; // Thành tích

    @Property("isPublic")
    Boolean isPublic;

    @TargetNode
    Location location;
}
