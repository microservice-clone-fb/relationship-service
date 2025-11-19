package com.tam.relationship.repository;

import com.tam.relationship.entity.UserUserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWithUserRepository extends JpaRepository<UserUserRelationship, String> {
    @Query("SELECT r FROM UserUserRelationship r WHERE r.user1.id = ?1 OR r.user2.id = ?1")
    List<UserUserRelationship> findAllRelationshipBetweenUserAndUserByOneUserId(String userId);
}
