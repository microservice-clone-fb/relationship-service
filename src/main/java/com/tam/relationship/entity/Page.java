package com.tam.relationship.entity;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Đại diện cho Page/Fan Page trong hệ thống Neo4j
 * Node này lưu trữ thông tin về trang trên Facebook
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("Page")
public class Page {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    // pageInfo lấy từ profile-service bằng id ở đây

}
