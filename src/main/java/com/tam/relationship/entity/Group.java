package com.tam.relationship.entity;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Đại diện cho Group/Nhóm trong hệ thống Neo4j
 * Node này lưu trữ thông tin về nhóm trên Facebook
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("Group")
public class Group {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;
}
