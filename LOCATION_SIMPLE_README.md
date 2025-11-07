# Location Entities Documentation (Simplified Version)

## 📍 Tổng quan

Hệ thống Location đơn giản với **1 Node (Location)** và **1 Relationship (UserLocationRelationship)** giữa User và Location.

---

## 🏢 Location Node

### Location.java
**Mô tả**: Đại diện cho một địa điểm bất kỳ (thành phố, trường học, công ty, nhà hàng, v.v.)

**Thuộc tính**:
- `id`: ID tự động tạo
- `locationId`: ID từ location-service (nếu có)
- `name`: Tên địa điểm
- `type`: Loại địa điểm (CITY, SCHOOL, COMPANY, RESTAURANT, etc.)
- `address`: Địa chỉ đầy đủ
- `city`: Thành phố
- `state`: Tỉnh/Bang
- `country`: Quốc gia
- `postalCode`: Mã bưu điện
- `latitude`: Vĩ độ (cho Google Maps)
- `longitude`: Kinh độ (cho Google Maps)
- `category`: Danh mục (EDUCATION, WORK, RESIDENCE, ENTERTAINMENT, etc.)
- `description`: Mô tả
- `createdAt`: Thời gian tạo
- `isActive`: Trạng thái hoạt động

---

## 🔗 UserLocationRelationship

### UserLocationRelationship.java
**Type**: `RELATED_TO_LOCATION`  
**Mô tả**: Mối quan hệ **đơn giản và linh hoạt** giữa User và Location

**Thuộc tính**:
- `type`: Loại quan hệ - **LIVES_IN**, **STUDIED_AT**, **WORKS_AT**, **VISITED**, **HOMETOWN**, v.v.
- `startDate`: Thời điểm bắt đầu (optional)
- `endDate`: Thời điểm kết thúc (optional)
- `isCurrent`: Có hiện tại không (đang sống/làm/học tại đây)
- `description`: Mô tả thêm (vị trí công việc, chuyên ngành, đánh giá, v.v.)
- `isPublic`: Có công khai không
- `metadata`: Thông tin bổ sung dạng **JSON** (rất linh hoạt!)
- `createdAt`: Thời điểm tạo

**✅ Ưu điểm**:
- ✨ Đơn giản, dễ hiểu
- 🚀 Linh hoạt - có thể biểu diễn **mọi loại** quan hệ
- 🔧 Dễ mở rộng qua trường `metadata`
- 📦 Không cần nhiều entity phức tạp

---

## 🎯 Use Cases & Examples

### 1. User sống tại thành phố
```java
UserLocationRelationship livesIn = UserLocationRelationship.builder()
    .type("LIVES_IN")
    .isCurrent(true)
    .isPublic(true)
    .location(hanoiCity)
    .createdAt(LocalDateTime.now())
    .build();
```

**Cypher Query**:
```cypher
MATCH (u:User {userId: $userId})-[r:RELATED_TO_LOCATION {type: 'LIVES_IN', isCurrent: true}]->(loc:Location)
RETURN loc.name as currentCity
```

---

### 2. User học tại trường
```java
UserLocationRelationship studiedAt = UserLocationRelationship.builder()
    .type("STUDIED_AT")
    .description("Computer Science major")
    .startDate(LocalDateTime.of(2018, 9, 1, 0, 0))
    .endDate(LocalDateTime.of(2022, 6, 30, 0, 0))
    .isCurrent(false)
    .metadata("{\"degree\":\"Bachelor\",\"gpa\":3.8,\"activities\":[\"ACM\",\"Football\"]}")
    .location(university)
    .build();
```

**Cypher Query**:
```cypher
MATCH (u:User {userId: $userId})-[r:RELATED_TO_LOCATION {type: 'STUDIED_AT'}]->(school:Location)
RETURN school.name, r.description, r.startDate, r.endDate
ORDER BY r.startDate DESC
```

---

### 3. User làm việc tại công ty
```java
UserLocationRelationship worksAt = UserLocationRelationship.builder()
    .type("WORKS_AT")
    .description("Senior Software Engineer")
    .startDate(LocalDateTime.of(2022, 7, 1, 0, 0))
    .isCurrent(true)
    .metadata("{\"department\":\"Engineering\",\"employmentType\":\"FULL_TIME\"}")
    .location(company)
    .build();
```

**Cypher Query**:
```cypher
MATCH (u:User {userId: $userId})-[r:RELATED_TO_LOCATION {type: 'WORKS_AT', isCurrent: true}]->(company:Location)
RETURN company.name, r.description as position
```

---

### 4. User check-in địa điểm
```java
UserLocationRelationship visited = UserLocationRelationship.builder()
    .type("VISITED")
    .description("Great coffee and atmosphere!")
    .isCurrent(false)
    .metadata("{\"rating\":5,\"photos\":[\"photo1\",\"photo2\"],\"taggedUsers\":[\"user1\"]}")
    .createdAt(LocalDateTime.now())
    .location(cafe)
    .build();
```

**Cypher Query**:
```cypher
MATCH (u:User {userId: $userId})-[r:RELATED_TO_LOCATION {type: 'VISITED'}]->(loc:Location)
RETURN loc.name, r.description as review, r.createdAt
ORDER BY r.createdAt DESC
LIMIT 10
```

---

### 5. Quê quán
```java
UserLocationRelationship hometown = UserLocationRelationship.builder()
    .type("HOMETOWN")
    .description("Nơi sinh ra và lớn lên")
    .isPublic(true)
    .location(haiPhongCity)
    .build();
```

---

## 📊 Cypher Queries Thường Dùng

### 🔍 Tìm người cùng thành phố
```cypher
MATCH (me:User {userId: $myId})-[:RELATED_TO_LOCATION {type: 'LIVES_IN', isCurrent: true}]->(city:Location)
      <-[:RELATED_TO_LOCATION {type: 'LIVES_IN', isCurrent: true}]-(other:User)
WHERE me <> other
  AND NOT (me)-[:FRIEND_WITH]-(other)
RETURN other.userId, city.name, COUNT(*) as connections
ORDER BY connections DESC
LIMIT 10
```

### 🎓 Tìm cựu học sinh cùng trường
```cypher
MATCH (me:User {userId: $myId})-[r1:RELATED_TO_LOCATION {type: 'STUDIED_AT'}]->(school:Location)
      <-[r2:RELATED_TO_LOCATION {type: 'STUDIED_AT'}]-(alumnus:User)
WHERE me <> alumnus
  AND NOT (me)-[:FRIEND_WITH]-(alumnus)
RETURN alumnus.userId, school.name, r1.description as myMajor, r2.description as theirMajor
LIMIT 10
```

### 💼 Tìm đồng nghiệp
```cypher
MATCH (me:User {userId: $myId})-[:RELATED_TO_LOCATION {type: 'WORKS_AT', isCurrent: true}]->(company:Location)
      <-[:RELATED_TO_LOCATION {type: 'WORKS_AT', isCurrent: true}]-(colleague:User)
WHERE me <> colleague
RETURN colleague.userId, company.name, colleague.description as position
```

### 🗺️ Lịch sử check-in (Travel Map)
```cypher
MATCH (user:User {userId: $userId})-[r:RELATED_TO_LOCATION {type: 'VISITED'}]->(loc:Location)
WHERE r.isPublic = true
RETURN loc.name, loc.latitude, loc.longitude, r.description, r.createdAt, r.metadata
ORDER BY r.createdAt DESC
```

### 📋 Profile về nơi ở và làm việc
```cypher
MATCH (user:User {userId: $userId})-[r:RELATED_TO_LOCATION]->(loc:Location)
WHERE r.type IN ['LIVES_IN', 'WORKS_AT', 'HOMETOWN'] 
  AND (r.isCurrent = true OR r.type = 'HOMETOWN')
RETURN r.type, loc.name, r.description, r.isCurrent
```

### 📍 Gợi ý địa điểm dựa trên bạn bè
```cypher
MATCH (me:User {userId: $myId})-[:FRIEND_WITH]-(friend:User)-[v:RELATED_TO_LOCATION {type: 'VISITED'}]->(loc:Location)
WHERE NOT (me)-[:RELATED_TO_LOCATION {type: 'VISITED'}]->(loc)
RETURN loc.name, loc.type, COUNT(friend) as friendsVisited
ORDER BY friendsVisited DESC
LIMIT 10
```

---

## 🌟 Ví dụ Metadata JSON

### Cho STUDIED_AT
```json
{
  "degree": "Bachelor",
  "major": "Computer Science",
  "minor": "Mathematics",
  "gpa": 3.8,
  "activities": ["ACM Club", "Football Team"],
  "graduated": true,
  "honors": ["Dean's List"]
}
```

### Cho WORKS_AT
```json
{
  "department": "Engineering",
  "employmentType": "FULL_TIME",
  "responsibilities": ["Backend Development", "Team Lead"],
  "skills": ["Java", "Spring Boot", "Neo4j"]
}
```

### Cho VISITED
```json
{
  "rating": 5,
  "photos": ["photo_id_1", "photo_id_2"],
  "taggedUsers": ["user_id_1", "user_id_2"],
  "occasion": "Birthday celebration"
}
```

---

## 📋 Các loại Type quan trọng

### Relationship Types (trong trường `type`)
- `LIVES_IN` - Đang sống tại
- `HOMETOWN` - Quê quán
- `STUDIED_AT` - Đã học tại
- `WORKS_AT` - Làm việc tại
- `VISITED` - Đã ghé thăm
- `INTERESTED_IN` - Quan tâm đến địa điểm
- `CHECKED_IN` - Check-in
- `FAVORITE` - Địa điểm yêu thích

### Location Types (trong Location.type)
```java
CITY, COUNTRY, SCHOOL, UNIVERSITY, COMPANY, 
RESTAURANT, CAFE, HOTEL, TOURIST_SPOT, 
PARK, SHOPPING_MALL, OTHER
```

---

## 📈 Sơ đồ đơn giản

```
      USER
        │
        │ RELATED_TO_LOCATION
        │ {
        │   type: "LIVES_IN" | "STUDIED_AT" | "WORKS_AT" | "VISITED" | ...
        │   isCurrent: true/false
        │   description: "..."
        │   metadata: {...}
        │   startDate: ...
        │   endDate: ...
        │ }
        │
        ▼
    LOCATION
    {
      name: "..."
      type: "CITY" | "SCHOOL" | "COMPANY" | ...
      address: "..."
      city: "..."
      country: "..."
      latitude: ...
      longitude: ...
    }
```

---

## ✅ Ưu điểm của thiết kế này

1. **Cực kỳ đơn giản**: Chỉ 1 relationship type thay vì 4-5 types
2. **Linh hoạt**: Có thể biểu diễn **mọi loại** quan hệ qua trường `type`
3. **Dễ mở rộng**: Thêm loại quan hệ mới chỉ cần thêm giá trị cho `type`
4. **Metadata JSON**: Có thể lưu thông tin bổ sung tùy ý
5. **Dễ query**: Chỉ cần query 1 relationship type với filter
6. **Maintainable**: Ít code hơn, dễ maintain hơn
7. **Performance**: Ít relationship types = query nhanh hơn

---

## 🚀 Implementation Tips

### 1. Tạo Index
```cypher
CREATE INDEX location_name FOR (l:Location) ON (l.name);
CREATE INDEX location_type FOR (l:Location) ON (l.type);
CREATE INDEX location_city FOR (l:Location) ON (l.city);
CREATE INDEX location_country FOR (l:Location) ON (l.country);
```

### 2. Geospatial Queries
```cypher
// Tìm địa điểm trong bán kính 5km
MATCH (loc:Location)
WHERE point.distance(
  point({latitude: loc.latitude, longitude: loc.longitude}),
  point({latitude: $myLat, longitude: $myLon})
) < 5000
RETURN loc
```

### 3. Parse Metadata JSON trong Java
```java
ObjectMapper mapper = new ObjectMapper();
Map<String, Object> metadata = mapper.readValue(relationship.getMetadata(), Map.class);
Integer rating = (Integer) metadata.get("rating");
List<String> photos = (List<String>) metadata.get("photos");
```

---

**Created**: 2025  
**Version**: 2.0 (Simplified & Clean) ✨
