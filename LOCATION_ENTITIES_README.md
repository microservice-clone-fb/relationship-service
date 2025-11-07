# Location Entities Documentation

## 📍 Tổng quan

Hệ thống Location entities quản lý các địa điểm và mối quan hệ giữa người dùng với địa điểm trên mạng xã hội Facebook.

## 🏢 Location Node

### Location.java
**Mô tả**: Đại diện cho một địa điểm (thành phố, trường học, công ty, nhà hàng, v.v.)

**Thuộc tính**:
- `id`: ID tự động tạo
- `locationId`: ID từ location-service
- `name`: Tên địa điểm
- `type`: Loại địa điểm (CITY, SCHOOL, COMPANY, RESTAURANT, etc.)
- `address`: Địa chỉ đầy đủ
- `city`: Thành phố
- `state`: Tỉnh/Bang
- `country`: Quốc gia
- `postalCode`: Mã bưu điện
- `latitude`: Vĩ độ (cho Google Maps)
- `longitude`: Kinh độ (cho Google Maps)
- `category`: Danh mục (EDUCATION, WORK, RESIDENCE, etc.)
- `description`: Mô tả
- `createdAt`: Thời gian tạo
- `isActive`: Trạng thái hoạt động

## 🔗 User-Location Relationships

### 1. LivesInRelationship.java
**Type**: `LIVES_IN`
**Mô tả**: User đang sống hoặc đã từng sống tại địa điểm

**Thuộc tính**:
- `type`: CURRENT (hiện tại), HOMETOWN (quê quán), PREVIOUS (trước đây)
- `startedAt`: Thời điểm bắt đầu sống
- `endedAt`: Thời điểm kết thúc (nếu không còn sống ở đó)
- `isCurrent`: Có đang sống tại đây không
- `isHometown`: Có phải quê quán không
- `isPublic`: Có công khai không
- `description`: Mô tả thêm

**Use Cases**:
- Hiển thị nơi ở hiện tại trên profile
- Hiển thị quê quán
- Tìm người cùng quê
- Gợi ý kết bạn dựa trên địa điểm

**Ví dụ**:
```
User "John" -[LIVES_IN {type: "CURRENT", isCurrent: true}]-> Location "Hanoi"
User "Jane" -[LIVES_IN {type: "HOMETOWN", isHometown: true}]-> Location "Hai Phong"
```

---

### 2. StudiedAtRelationship.java
**Type**: `STUDIED_AT`
**Mô tả**: User đã học hoặc đang học tại trường/đại học

**Thuộc tính**:
- `schoolName`: Tên trường
- `degree`: Bậc học (ELEMENTARY, MIDDLE_SCHOOL, HIGH_SCHOOL, UNIVERSITY, etc.)
- `major`: Chuyên ngành chính
- `minor`: Chuyên ngành phụ
- `startYear`: Năm bắt đầu
- `endYear`: Năm kết thúc/tốt nghiệp
- `graduated`: Đã tốt nghiệp chưa
- `gpa`: Điểm trung bình (optional)
- `activities`: Hoạt động, câu lạc bộ
- `isCurrentStudent`: Có đang học không
- `isPublic`: Có công khai không
- `description`: Mô tả

**Use Cases**:
- Hiển thị học vấn trên profile
- Tìm bạn học cùng trường
- Gợi ý kết bạn với cựu học sinh
- Network theo trường/ngành học
- Alumni connections

**Ví dụ**:
```
User "John" -[STUDIED_AT {
  schoolName: "HUST",
  degree: "UNIVERSITY",
  major: "Computer Science",
  startYear: 2018,
  endYear: 2022,
  graduated: true,
  isCurrentStudent: false
}]-> Location "Hanoi University of Science and Technology"
```

---

### 3. WorksAtRelationship.java
**Type**: `WORKS_AT`
**Mô tả**: User đang làm hoặc đã từng làm việc tại công ty/địa điểm

**Thuộc tính**:
- `companyName`: Tên công ty
- `position`: Vị trí công việc (Software Engineer, Manager, etc.)
- `department`: Phòng ban (Engineering, Marketing, etc.)
- `employmentType`: Loại hình (FULL_TIME, PART_TIME, CONTRACT, FREELANCE, INTERNSHIP)
- `startDate`: Ngày bắt đầu
- `endDate`: Ngày kết thúc (nếu đã nghỉ)
- `isCurrent`: Có đang làm việc không
- `description`: Mô tả công việc
- `responsibilities`: Trách nhiệm
- `achievements`: Thành tích
- `isPublic`: Có công khai không

**Use Cases**:
- Hiển thị kinh nghiệm làm việc trên profile
- Tìm đồng nghiệp
- Professional networking
- Gợi ý kết bạn với người cùng công ty
- Job recommendations

**Ví dụ**:
```
User "Jane" -[WORKS_AT {
  companyName: "VNG Corporation",
  position: "Senior Software Engineer",
  department: "Engineering",
  employmentType: "FULL_TIME",
  startDate: "2020-01-15",
  isCurrent: true
}]-> Location "VNG Campus HCM"
```

---

### 4. VisitedRelationship.java
**Type**: `VISITED`
**Mô tả**: User đã check-in hoặc ghé thăm địa điểm

**Thuộc tính**:
- `visitedAt`: Thời điểm ghé thăm
- `visitCount`: Số lần ghé thăm
- `rating`: Đánh giá (1-5 sao)
- `review`: Nhận xét/review
- `photos`: Danh sách ID ảnh (JSON array)
- `isPublic`: Có công khai không
- `taggedUsers`: Danh sách user ID được tag cùng (JSON array)

**Use Cases**:
- Check-in địa điểm
- Review địa điểm
- Chia sẻ trải nghiệm
- Gợi ý địa điểm dựa trên lịch sử
- Travel history
- Tìm người đã đến cùng địa điểm

**Ví dụ**:
```
User "John" -[VISITED {
  visitedAt: "2024-12-01T10:30:00",
  visitCount: 1,
  rating: 5,
  review: "Great coffee and atmosphere!",
  isPublic: true
}]-> Location "The Coffee House"
```

---

## 📋 Location Enums

### 1. LocationType.java
```java
CITY, COUNTRY, SCHOOL, UNIVERSITY, COLLEGE, COMPANY, WORKPLACE,
RESTAURANT, CAFE, HOTEL, TOURIST_SPOT, SHOPPING_MALL, HOSPITAL,
PARK, GYM, LIBRARY, MUSEUM, THEATER, STADIUM, AIRPORT, 
TRAIN_STATION, BUS_STATION, OTHER
```

### 2. EducationLevel.java
```java
ELEMENTARY,         // Tiểu học
MIDDLE_SCHOOL,      // Trung học cơ sở
HIGH_SCHOOL,        // Trung học phổ thông
VOCATIONAL,         // Trung cấp nghề
COLLEGE,            // Cao đẳng
UNIVERSITY,         // Đại học
BACHELOR,           // Cử nhân
MASTER,             // Thạc sĩ
DOCTORATE,          // Tiến sĩ
POSTDOC,            // Sau tiến sĩ
OTHER
```

### 3. EmploymentType.java
```java
FULL_TIME,          // Toàn thời gian
PART_TIME,          // Bán thời gian
CONTRACT,           // Hợp đồng
TEMPORARY,          // Tạm thời
FREELANCE,          // Tự do
INTERNSHIP,         // Thực tập
VOLUNTEER,          // Tình nguyện
SEASONAL,           // Theo mùa
SELF_EMPLOYED,      // Tự kinh doanh
OTHER
```

### 4. ResidenceType.java
```java
CURRENT,        // Nơi đang sống hiện tại
HOMETOWN,       // Quê quán
PREVIOUS,       // Nơi đã từng sống
TEMPORARY       // Nơi tạm trú
```

---

## 🎯 Use Cases và Cypher Queries

### 1. Tìm người cùng quê
```cypher
MATCH (me:User {userId: $myId})-[r1:LIVES_IN {isHometown: true}]->(loc:Location)
      <-[r2:LIVES_IN {isHometown: true}]-(other:User)
WHERE me <> other
RETURN other, loc.name as hometown
```

### 2. Tìm cựu học sinh cùng trường
```cypher
MATCH (me:User {userId: $myId})-[r1:STUDIED_AT]->(school:Location)
      <-[r2:STUDIED_AT]-(alumnus:User)
WHERE me <> alumnus 
  AND r1.schoolName = r2.schoolName
  AND NOT (me)-[:FRIEND_WITH]-(alumnus)
RETURN alumnus, school.name, r2.startYear, r2.endYear
ORDER BY r2.endYear DESC
```

### 3. Tìm đồng nghiệp (cùng công ty)
```cypher
MATCH (me:User {userId: $myId})-[r1:WORKS_AT {isCurrent: true}]->(company:Location)
      <-[r2:WORKS_AT {isCurrent: true}]-(colleague:User)
WHERE me <> colleague
  AND r1.companyName = r2.companyName
RETURN colleague, r2.position, r2.department
```

### 4. Gợi ý địa điểm dựa trên bạn bè
```cypher
MATCH (me:User {userId: $myId})-[:FRIEND_WITH]-(friend:User)-[v:VISITED]->(loc:Location)
WHERE NOT (me)-[:VISITED]->(loc)
  AND v.rating >= 4
RETURN loc, COUNT(friend) as friendsVisited, AVG(v.rating) as avgRating
ORDER BY friendsVisited DESC, avgRating DESC
LIMIT 10
```

### 5. Tìm người đang sống gần tôi
```cypher
MATCH (me:User {userId: $myId})-[r1:LIVES_IN {isCurrent: true}]->(myCity:Location)
MATCH (other:User)-[r2:LIVES_IN {isCurrent: true}]->(myCity)
WHERE me <> other
  AND NOT (me)-[:FRIEND_WITH]-(other)
RETURN other, myCity.name
```

### 6. Profile Education History
```cypher
MATCH (user:User {userId: $userId})-[s:STUDIED_AT]->(school:Location)
RETURN school.name, s.degree, s.major, s.startYear, s.endYear, s.graduated
ORDER BY s.startYear DESC
```

### 7. Profile Work Experience
```cypher
MATCH (user:User {userId: $userId})-[w:WORKS_AT]->(company:Location)
RETURN company.name, w.companyName, w.position, w.employmentType, 
       w.startDate, w.endDate, w.isCurrent
ORDER BY w.isCurrent DESC, w.startDate DESC
```

### 8. Travel Map (Các địa điểm đã ghé thăm)
```cypher
MATCH (user:User {userId: $userId})-[v:VISITED]->(loc:Location)
WHERE v.isPublic = true
RETURN loc.name, loc.latitude, loc.longitude, v.visitedAt, 
       v.rating, v.visitCount
ORDER BY v.visitedAt DESC
```

### 9. Tìm người học cùng khóa
```cypher
MATCH (me:User {userId: $myId})-[r1:STUDIED_AT]->(school:Location)
      <-[r2:STUDIED_AT]-(classmate:User)
WHERE me <> classmate
  AND r1.startYear = r2.startYear
  AND r1.endYear = r2.endYear
  AND r1.major = r2.major
RETURN classmate, school.name, r2.major
```

### 10. Gợi ý việc làm dựa trên network
```cypher
MATCH (me:User {userId: $myId})-[:FRIEND_WITH]-(friend:User)-[w:WORKS_AT {isCurrent: true}]->(company:Location)
WHERE NOT (me)-[:WORKS_AT]->(company)
RETURN company.name, company.type, COUNT(friend) as friendsWorking
ORDER BY friendsWorking DESC
LIMIT 10
```

---

## 🌟 Tính năng nâng cao

### 1. Location-based Friend Suggestions
Gợi ý kết bạn dựa trên:
- Cùng quê quán
- Cùng thành phố đang sống
- Cùng trường học
- Cùng công ty
- Đã check-in cùng địa điểm

### 2. Professional Networking
- Tìm người cùng ngành
- Alumni network
- Company connections
- Industry insights

### 3. Travel & Discovery
- Recommend places based on friends' visits
- Travel buddies
- Local guides
- Event locations

### 4. Privacy Controls
- Hide current location
- Hide work information
- Hide education details
- Public/Friends-only visibility

---

## 📊 Sơ đồ Relationships

```
      USER
        │
        ├── LIVES_IN ──────────► LOCATION (City/Country)
        │   ├─ Current residence
        │   ├─ Hometown
        │   └─ Previous residence
        │
        ├── STUDIED_AT ────────► LOCATION (School/University)
        │   ├─ Education level
        │   ├─ Major/Minor
        │   └─ Graduation status
        │
        ├── WORKS_AT ──────────► LOCATION (Company/Workplace)
        │   ├─ Position
        │   ├─ Employment type
        │   └─ Current/Previous
        │
        └── VISITED ───────────► LOCATION (Any place)
            ├─ Check-ins
            ├─ Ratings & Reviews
            └─ Travel history
```

---

## 🚀 Implementation Tips

### 1. Indexes
```cypher
CREATE INDEX location_name FOR (l:Location) ON (l.name);
CREATE INDEX location_type FOR (l:Location) ON (l.type);
CREATE INDEX location_city FOR (l:Location) ON (l.city);
CREATE INDEX location_country FOR (l:Location) ON (l.country);
```

### 2. Geospatial Queries
Sử dụng `latitude` và `longitude` để tìm địa điểm gần:
```cypher
MATCH (loc:Location)
WHERE point.distance(
  point({latitude: loc.latitude, longitude: loc.longitude}),
  point({latitude: $myLat, longitude: $myLon})
) < 5000 // 5km radius
RETURN loc
```

### 3. Full-text Search
```cypher
CALL db.index.fulltext.createNodeIndex(
  "locationSearch",
  ["Location"],
  ["name", "address", "city"]
)
```

---

## 📝 Notes

- Location nodes có thể được share giữa nhiều users
- Một user có thể có nhiều STUDIED_AT relationships (nhiều trường)
- Một user có thể có nhiều WORKS_AT relationships (nhiều công việc)
- VISITED relationships ghi lại lịch sử, không xóa khi user rời khỏi
- Privacy settings quan trọng cho location data

---

**Created**: 2025
**Version**: 1.0
