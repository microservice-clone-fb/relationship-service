package com.tam.relationship.entity;

import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Đại diện cho Địa điểm trong hệ thống Neo4j
 * Node này lưu trữ thông tin về địa điểm (thành phố, quốc gia, trường học, công
 * ty, v.v.)
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("Location")
public class Location {

    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;
}
