package com.tam.relationship.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tam.relationship.entity.UserUserRelationship;
import com.tam.relationship.entity.enums.RelationshipStatus;
import com.tam.relationship.entity.enums.UserUserRelationType;

@Repository
public interface UserWithUserRepository extends JpaRepository<UserUserRelationship, String> {
    @Query("SELECT r FROM UserUserRelationship r WHERE r.user1.userId = :userId OR r.user2.userId = :userId")
    List<UserUserRelationship> findAllRelationshipBetweenUserAndUserByOneUserId(@Param("userId") String userId);

    @Query(
            """
			SELECT r FROM UserUserRelationship r
			WHERE r.relationType = :relationType
			AND (
				(r.user1.userId = :userId AND r.user2.userId = :targetUserId)
				OR
				(r.user1.userId = :targetUserId AND r.user2.userId = :userId)
			)
			""")
    Optional<UserUserRelationship> findRelationshipBetweenUsers(
            @Param("userId") String userId,
            @Param("targetUserId") String targetUserId,
            @Param("relationType") UserUserRelationType relationType);

    @Query(
            """
			SELECT r FROM UserUserRelationship r
			WHERE r.relationType = :relationType
			AND r.user1.userId = :requesterId
			AND r.user2.userId = :targetUserId
			AND r.status = :status
			""")
    Optional<UserUserRelationship> findDirectedRelationship(
            @Param("requesterId") String requesterId,
            @Param("targetUserId") String targetUserId,
            @Param("relationType") UserUserRelationType relationType,
            @Param("status") RelationshipStatus status);
}
